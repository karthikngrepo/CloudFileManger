package com.dev.mmx.domain.dao;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.dev.mmx.domain.constant.CommonConstants;
import com.dev.mmx.domain.constant.DAOConstants;
import com.dev.mmx.domain.exception.CoreException;
import com.dev.mmx.domain.util.CommonUtilities;
import com.dev.mmx.domain.util.DAOUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@Repository
@PropertySource("classpath:db.properties")
public class DocumentDAO {

	private Logger log = Logger.getLogger(DocumentDAO.class);
	
	@Autowired
    Environment env;
	
	/**
	 * This method handles the logic associated with saving the document from client app
	 * to mongo DB.
	 * 
	 * @param request
	 * @throws Exception
	 */
	public void saveDocumentToDB(HttpServletRequest request) throws Exception {
		FileOutputStream fos = null;
		ServletInputStream servletInputStream = null;
		MongoClient mongo = null;
		byte[] buffer = new byte[1 * 1024 * 1024];
				
		try {
			String fileName = CommonUtilities.getMetaData(request, CommonConstants.FILE_NAME);
			log.debug("File name recieved from request : "+fileName);
			
			servletInputStream = request.getInputStream();
			servletInputStream.read(buffer);

			fos = new FileOutputStream(fileName);
			fos.write(buffer);

			mongo = new MongoClient(env.getProperty(DAOConstants.MONGO_DB_HOST)
					, Integer.parseInt(env.getProperty(DAOConstants.MONGO_DB_PORT)));

			DB db = mongo.getDB(env.getProperty(DAOConstants.MONGO_DB_NAME));

			Path path = Paths.get(fileName);
			log.debug("Document is expected in location : "+path.toAbsolutePath());
			if(!Files.exists(path)) {
				throw new CoreException("Failed to locate the document locally !!!");
			}

			GridFS gfs = new GridFS(db, DAOConstants.TABLE_DOCUMENT_BACKUP);

			GridFSInputFile inputFile = gfs.createFile(path.toFile());
			inputFile.setContentType(CommonUtilities.getMetaData(request, CommonConstants.FILE_CONTENTTYPE));
			inputFile.setFilename(path.getFileName().toString());

			DBObject dbObject = new BasicDBObject();
			dbObject.put(CommonConstants.FILE_ACCESSED, DAOUtil.sdf.parse(CommonUtilities.getMetaData(request, CommonConstants.FILE_ACCESSED)));
			dbObject.put(CommonConstants.FILE_CONTENTTYPE, CommonUtilities.getMetaData(request, CommonConstants.FILE_CONTENTTYPE));
			dbObject.put(CommonConstants.FILE_CREATED, DAOUtil.sdf.parse(CommonUtilities.getMetaData(request, CommonConstants.FILE_CREATED)));
			dbObject.put(CommonConstants.FILE_SIZE, CommonUtilities.getFileSize(path));
			dbObject.put(CommonConstants.FILE_TYPE, CommonUtilities.getMetaData(request, CommonConstants.FILE_TYPE));
			dbObject.put(CommonConstants.FILE_LAST_MODIFIED, DAOUtil.sdf.parse(CommonUtilities.getMetaData(request, CommonConstants.FILE_LAST_MODIFIED)));
			dbObject.put(CommonConstants.FILE_OWNER, CommonUtilities.getMetaData(request, CommonConstants.FILE_OWNER));
			dbObject.put(CommonConstants.RECORD_CREATED, new Date());
			inputFile.setMetaData(dbObject);
			inputFile.save();

			log.debug("Successfully saved document to DB ...");
			mongo.close();

		} catch (Exception e) {
			e.printStackTrace();
			
			try {
				if(mongo != null) {
					mongo.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			throw e;
		} finally { 
			try {
				if(mongo != null) {
					mongo.close();
				}
				if(fos != null) {
					fos.close();
				}
				if(servletInputStream != null) {
					servletInputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}

	public List<GridFSDBFile> getDocuments(Path path) throws Exception {
		MongoClient mongo = null;
		List<GridFSDBFile> filesFromDB = new ArrayList<GridFSDBFile>();
		try {
			log.debug("Mongo DB details = "+env.getProperty(DAOConstants.MONGO_DB_HOST)
					+ ":"+env.getProperty(DAOConstants.MONGO_DB_PORT));
			
			mongo = new MongoClient(env.getProperty(DAOConstants.MONGO_DB_HOST)
					, Integer.parseInt(env.getProperty(DAOConstants.MONGO_DB_PORT)));

			DB db = mongo.getDB(env.getProperty(DAOConstants.MONGO_DB_NAME));
			GridFS gfs = new GridFS(db, DAOConstants.TABLE_DOCUMENT_BACKUP);

			filesFromDB.addAll(gfs.find(path.getFileName().toString()));

			log.debug("Successfully retrived documents from DB ...");
			mongo.close();

		} catch (Exception e) {
			e.printStackTrace();

			try {
				if(mongo != null) {
					mongo.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			throw e;
		} finally { 
			try {
				if(mongo != null) {
					mongo.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return filesFromDB;
	}
}
