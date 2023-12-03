package com.example.eventqrcode.controller;

import android.app.Activity;
import android.content.ActivityNotFoundException;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import com.example.eventqrcode.MainActivity;
import com.example.eventqrcode.dao.PessoaDAO;
import com.example.eventqrcode.model.Evento;
import com.example.eventqrcode.model.Pessoa;

import com.example.eventqrcode.dao.EventoDAO;
import com.example.eventqrcode.util.PdfCreator;
import com.google.zxing.integration.android.IntentIntegrator;
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
        BarcodeQRCode b = new BarcodeQRCode(idPessoa.toString(), 200, 200, null);
        return b.getImage();
    }

    public void lerQrCode(Activity activity){
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("");
        integrator.initiateScan();
    }

    public void registrarSaida(Context context, Integer idPessoa) {
        PessoaDAO pessoaDAO = new PessoaDAO(context);
        Pessoa p = pessoaDAO.find(idPessoa);
        try {
            EventoDAO eventoDAO = new EventoDAO(context);
            Evento e = eventoDAO.find(p.getEventoID());
            e.setPessoas(e.getPessoas()-1);
            eventoDAO.update(e);
        } catch (Exception err){
            Toast.makeText(context, "QRCode inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        /*
        long unix = System.currentTimeMillis() / 1000L;
        p.setHoraSaida(Integer.parseInt(unix + ""));
         */


        pessoaDAO.delete(idPessoa);
        Toast.makeText(context, "Saída registrada com sucesso", Toast.LENGTH_SHORT).show();
    }

    public void finalizarEvento(Context context, Integer idEvento) {
        EventoDAO eventoDAO = new EventoDAO(context);
        PessoaDAO pessoaDAO = new PessoaDAO(context);

        pessoaDAO.deleteMany(idEvento);

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

    public Uri gerarPdf(Context context, String idPessoa, Image img) throws IOException, DocumentException {
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
            File directoryRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
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
            //visualizarPdf(context, uri);
            return uri;
        }else{
            visualizarPdf(pdf);
        }

        return uri;
    }

    //versoes antigas
    private void visualizarPdf(File pdf){

    }

    //versoes novas
    private void visualizarPdf(Context context, Uri uri){
        //Tamo fudido
    }
}
