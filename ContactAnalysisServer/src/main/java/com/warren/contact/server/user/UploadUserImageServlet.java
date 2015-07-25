package com.warren.contact.server.user;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.warren.contact.server.domain.Constants;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.UserService;
import com.warren.contact.server.service.implement.UserServiceImpl;

public class UploadUserImageServlet extends HttpServlet {
	Logger logger = Logger.getLogger(UploadUserImageServlet.class);
	UserService userService = new UserServiceImpl();
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory();		
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		//�����ϴ��ļ���С�����ޣ�-1��ʾ������ 
		fileUpload.setSizeMax(-1);
		//�ϴ��ļ����������а������ļ��ֶκ���ͨ�ֶ�
		try {
			List<FileItem> items  = fileUpload.parseRequest(request);
			if(items.size()>0) {
				 FileItem item = (FileItem) items.get(0);
		            String name = item.getName();
		            String path = getServletContext().getRealPath(Constants.UPLOAD_IMG_PATH) +"/"+ name;
		            logger.info("�ϴ��û�ͼƬ��Ϣ,ͼƬ����"+name+";�ϴ�·����"+path);
		            // ʵ��ͼƬ�ϴ�
		            try {
						item.write(new File(path));
						//���û�ͷ��·������.
						User user = new User();
						user.setPhone(request.getHeader("owner"));
						user.setImgPath(Constants.UPLOAD_IMG_PATH+"/"+name);
						userService.saveOrUpdateUserInfo(user);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		
			
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
