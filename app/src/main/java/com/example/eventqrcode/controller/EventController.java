package com.example.eventqrcode.controller;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
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
import com.itextpdf.text.pdf.qrcode.WriterException;
import com.itextpdf.text.pdf.*;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class EventController {

    Evento evento;
    Pessoa pessoa;
    EventoDAO eventoDao;
    PessoaDAO pessoaDAO;

    public void criarEvento(Context context, String nome, Integer maximoPessoas) {
        evento = new Evento(null, nome, maximoPessoas, null);
        eventoDao = new EventoDAO(context);
        eventoDao.create(evento);
    }

    public Evento pegarEvento(Context context, Integer id) {
        eventoDao = new EventoDAO(context);
        return eventoDao.find(id);
    }

    public ArrayList<Evento> listarEventos(Context context) {
        eventoDao = new EventoDAO(context);
        return eventoDao.findAll();
    }

    public boolean registrarEntrada(Context context, String nome, String cpf, Integer idEvento) {
        eventoDao = new EventoDAO(context);
        evento = eventoDao.find(idEvento);

        if(eventoEstaLotado()) return false;

        pessoa = new Pessoa(null, nome, cpf, idEvento, null, null);
        pessoaDAO = new PessoaDAO(context);
        pessoaDAO.create(pessoa);

        evento.setPessoas(evento.getPessoas() + 1);
        eventoDao.update(evento);
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
        pessoaDAO = new PessoaDAO(context);
        pessoa = pessoaDAO.find(idPessoa);
        try {
            eventoDao = new EventoDAO(context);
            evento = eventoDao.find(pessoa.getEventoID());
            evento.setPessoas(evento.getPessoas()-1);
            eventoDao.update(evento);
        } catch (Exception err){
            Toast.makeText(context, "QRCode inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        pessoaDAO.delete(idPessoa);
        Toast.makeText(context, "Saída registrada com sucesso", Toast.LENGTH_SHORT).show();
    }

    public void finalizarEvento(Context context, Integer idEvento) {
        eventoDao = new EventoDAO(context);
        pessoaDAO = new PessoaDAO(context);
        pessoaDAO.deleteMany(idEvento);
        eventoDao.delete(idEvento);
    }

    public ArrayList<Pessoa> listarPessoas(Context context, Integer idEvento) {
        pessoaDAO = new PessoaDAO(context);
        return pessoaDAO.getPessoasFromEvent(idEvento);
    }

    public Pessoa pegarPessoa(Context context, String cpf){
        pessoaDAO = new PessoaDAO(context);
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

        Pessoa pessoa = this.pegarPessoa(context, idPessoa);

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

        paragraph = new Paragraph(pessoa.getNome(), font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

        document.close();
        outputStream.close();
        descriptor.close();

        return uri;
    }

    private boolean eventoEstaLotado(){
        if(evento.getPessoas() == evento.getMaximoPessoas()){
            return false;
        }
        return true;
    }
}
