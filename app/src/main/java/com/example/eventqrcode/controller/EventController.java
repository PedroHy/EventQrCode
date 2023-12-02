package com.example.eventqrcode.controller;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.eventqrcode.dao.PessoaDAO;
import com.example.eventqrcode.model.Evento;
import com.example.eventqrcode.model.Pessoa;

import com.example.eventqrcode.dao.EventoDAO;
import com.example.eventqrcode.util.PdfCreator;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.qrcode.ByteMatrix;
import com.itextpdf.text.pdf.qrcode.QRCodeWriter;
import com.itextpdf.text.pdf.qrcode.WriterException;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EventController {

    public void criarEvento(Context context, String nome, Integer maximoPessoas) {
        Evento e = new Evento(null, nome, maximoPessoas, null);

        EventoDAO dao = new EventoDAO(context);

        dao.create(e);
    }

    public Evento pegarEvento(Context context, Integer id) {
        EventoDAO dao = new EventoDAO(context);

        return dao.find(id);
    }

    public ArrayList<Evento> listarEventos(Context context) {
        EventoDAO dao = new EventoDAO(context);

        return dao.findAll();
    }

    public boolean registrarEntrada(Context context, String nome, String cpf, Integer idEvento) {
        EventoDAO eventoDAO = new EventoDAO(context);
        Evento e = eventoDAO.find(idEvento);

        if(e.getPessoas() == e.getMaximoPessoas()){
            // Atingiu o maximo de pessoas
            return false;
        }

        Pessoa p = new Pessoa(null, nome, cpf, idEvento, null, null);
        PessoaDAO pessoaDAO = new PessoaDAO(context);
        pessoaDAO.create(p);


        e.setPessoas(e.getPessoas() + 1);
        eventoDAO.update(e);
        return true;
    }

    public Image gerarQrCode(Integer idPessoa) throws WriterException, BadElementException {
        BarcodeQRCode b = new BarcodeQRCode("Oi", 200, 200, null);
        return b.getImage();

        /*
        QRCodeWriter q = new QRCodeWriter();
        q.encode(idPessoa.toString(), , 100, 100);

        byte[] bArray = new byte[byteMatrix.getWidth() * byteMatrix.getHeight()];

        for(int i = 0; i < byteMatrix.getWidth(); i++){
            byte[] bLinha = byteMatrix.getArray()[i];

            for(int j = 0; j < byteMatrix.getHeight(); j++){
                byte b = bLinha[j];

                bArray[i*byteMatrix.getWidth() + j] = b;
            }
        }

        return bArray;
        */
    }

    public void registrarSaida(Context context, Integer idPessoa) {
        PessoaDAO pessoaDAO = new PessoaDAO(context);
        Pessoa p = pessoaDAO.find(idPessoa);

        long unix = System.currentTimeMillis() / 1000L;
        p.setHoraSaida(Integer.parseInt(unix + ""));

        pessoaDAO.delete(idPessoa);
    }

    public void finalizarEvento(Context context, Integer idEvento) {
        EventoDAO eventoDAO = new EventoDAO(context);

        eventoDAO.delete(idEvento);
    }

    public ArrayList<Pessoa> listarPessoas(Context context, Integer idEvento) {
        PessoaDAO pessoaDAO = new PessoaDAO(context);
        return pessoaDAO.getPessoasFromEvent(idEvento);
    }

    public Pessoa pegarPessoa(Context context, String cpf){
        PessoaDAO pessoaDAO = new PessoaDAO(context);
        return pessoaDAO.find(cpf);
    }

    public void gerarPdf(Context context, String idPessoa, Image img) throws IOException, DocumentException {
        File pdf = null;
        Uri uri = null;
        OutputStream outputStream = null;
        ParcelFileDescriptor descriptor = null;

        //salvando pdf no disposito dependendo da versão do android
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

            ContentValues contentValues = new ContentValues();

            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "QrCode"+idPessoa);
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            ContentResolver resolver = context.getContentResolver();
            uri = resolver.insert(MediaStore.Downloads.getContentUri("external"), contentValues);

            descriptor = resolver.openFileDescriptor(uri, "rw");
            FileDescriptor fileDescriptor = descriptor.getFileDescriptor();

            outputStream = new FileOutputStream(fileDescriptor);

        }else{
            File directoryRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/qrCodes/");
            File directory = new File(directoryRoot+"/qrCodes/");

            if(!directory.exists()){
                directory.mkdir();
            }

            String nomeArquivo = directory.getPath()+"/qrCode"+idPessoa+".pdf";
            pdf = new File(nomeArquivo);

            outputStream = new FileOutputStream(pdf);
        }

        //criando documento
        Rectangle rectangle = new Rectangle(226, 340);

        Document document = new Document(rectangle);
        PdfCreator pdfCreator = new PdfCreator();

        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);

        pdfWriter.setBoxSize("box", new Rectangle(0, 0, 0, 0));
        pdfWriter.setPageEvent(pdfCreator);

        document.open();

        String id = "ID Pessoa: " + idPessoa;
        Font font = new Font(Font.FontFamily.COURIER, 12, Font.NORMAL);

        Paragraph paragraph = new Paragraph("Código de entrada", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

        img.scaleToFit(150, 150);
        img.setAlignment(Element.ALIGN_CENTER);
        document.add(img);

        paragraph = new Paragraph(id, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

        document.close();
        outputStream.close();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            descriptor.close();
            visualizarPdf(context, uri);
        }else{
            visualizarPdf(pdf);
        }

    }

    //versoes antigas
    private void visualizarPdf(File pdf){

    }

    //versoes novas
    private void visualizarPdf(Context context, Uri uri){
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"application/pdf");

        List<ResolveInfo> lista = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if(lista.size() > 0){
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent1.setDataAndType(uri,"application/pdf");

            context.startActivity(intent1);
        }else{
            Toast.makeText(context.getApplicationContext(), "Você não possui leitores de pdf no seu dispositivo", Toast.LENGTH_LONG).show();
        }
    }
}
