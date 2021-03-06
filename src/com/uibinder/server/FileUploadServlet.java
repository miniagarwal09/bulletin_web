package com.uibinder.server;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class FileUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        if (! ServletFileUpload.isMultipartContent(request)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Not a multipart request");
            return;
        }
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath("/images/");
        fullPath.replace('/', '\\');
        ServletFileUpload upload = new ServletFileUpload(); // from Commons

        try {
            FileItemIterator iter = upload.getItemIterator(request);

            if (iter.hasNext()) {
                FileItemStream fileItem = iter.next();

                ServletOutputStream out = response.getOutputStream();
                response.setBufferSize(32768);
                InputStream in = fileItem.openStream();
                // The destination of your uploaded files
          
                File file = new File(fullPath+"\\"+fileItem.getName());
                
                OutputStream outputStream = new FileOutputStream(file);

                int length = 0;
                byte[] bytes = new byte[1024];

                while ((length = in.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, length);
                }

                response.setContentType("text/html");
                response.setHeader("Name",fileItem.getName());
                response.setContentLength(
                        (length > 0 && length <= Integer.MAX_VALUE) ? (int) length : 0);

                outputStream.close();
                in.close();
                out.flush();
                out.close();
            }
        } catch(Exception caught) {
            throw new RuntimeException(caught);
        }
    }
}
