package com.dev.mmx.domain.dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.dev.mmx.domain.beans.input.FileMetaData;
import com.dev.mmx.domain.constant.CommonConstants;
import com.dev.mmx.domain.constant.DAOConstants;
import com.dev.mmx.domain.exception.CoreException;
import com.dev.mmx.domain.util.DAOUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@Repository
@PropertySource("classpath:Queries.properties")
public class DocumentDAO {

//	@Autowired
//	private MongoTemplate mongoTemplate;
	
	@Autowired
    Environment env;
	
	public void saveDocument(FileMetaData metaData, Path path) throws Exception {
		MongoClient mongo = null;
		try {
			mongo = new MongoClient(env.getProperty(DAOConstants.MONGO_DB_HOST)
					, Integer.parseInt(env.getProperty(DAOConstants.MONGO_DB_PORT)));
			
			DB db = mongo.getDB(env.getProperty(DAOConstants.MONGO_DB_NAME));
			
			System.out.println("Document is expected in location : "+path.toAbsolutePath());
			if(!Files.exists(path)) {
				throw new CoreException("Failed to locate the document locally !!!");
			}
			
			GridFS gfs = new GridFS(db, DAOConstants.TABLE_DOCUMENT_BACKUP);

			GridFSInputFile inputFile = gfs.createFile(path.toFile());
			inputFile.setContentType(metaData.get_contentType());
			inputFile.setFilename(metaData.get_filename());
			
			DBObject dbObject = new BasicDBObject();
			dbObject.put(CommonConstants.FILE_ACCESSED, DAOUtil.sdf.format(metaData.get_accessed()));
			dbObject.put(CommonConstants.FILE_CONTENTTYPE, metaData.get_contentType());
			dbObject.put(CommonConstants.FILE_CREATED, DAOUtil.sdf.format(metaData.get_created()));
			dbObject.put(CommonConstants.FILE_SIZE, metaData.get_fileSize());
			dbObject.put(CommonConstants.FILE_TYPE, metaData.get_fileType());
			dbObject.put(CommonConstants.FILE_LAST_MODIFIED, DAOUtil.sdf.format(metaData.get_lastModified()));
			dbObject.put(CommonConstants.FILE_OWNER, metaData.get_owner());
			inputFile.setMetaData(dbObject);
			inputFile.save();
			
			System.out.println("Successfully saved document to DB ...");
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
		
	}
	
	public List<GridFSDBFile> getDocumentMetadata(FileMetaData[] multipleFileMetaData) throws Exception {
		MongoClient mongo = null;
		List<GridFSDBFile> filesFromDB = new ArrayList<GridFSDBFile>();
		try {
			System.out.println("Mongo host : "+env.getProperty(DAOConstants.MONGO_DB_HOST));
			System.out.println("Mongo port : "+env.getProperty(DAOConstants.MONGO_DB_PORT));
			mongo = new MongoClient(env.getProperty(DAOConstants.MONGO_DB_HOST)
					, Integer.parseInt(env.getProperty(DAOConstants.MONGO_DB_PORT)));

			DB db = mongo.getDB(DAOConstants.MONGO_DB_NAME);
			GridFS gfs = new GridFS(db, DAOConstants.TABLE_DOCUMENT_BACKUP);


			for(FileMetaData metaData : multipleFileMetaData) {
				if(metaData.get_filename() == null) {
					DBObject dbObject = new BasicDBObject();
					if(metaData.get_accessed() != null) {
						dbObject.put(CommonConstants.FILE_ACCESSED, metaData.get_accessed());
					}
					if(metaData.get_contentType() != null) {
						dbObject.put(CommonConstants.FILE_CONTENTTYPE, metaData.get_contentType());
					}
					if(metaData.get_created() != null) {
						dbObject.put(CommonConstants.FILE_CREATED, metaData.get_created());
					}
					if(metaData.get_fileSize() != null) {
						dbObject.put(CommonConstants.FILE_SIZE, metaData.get_fileSize());
					}
					if(metaData.get_fileType() != null) {
						dbObject.put(CommonConstants.FILE_TYPE, metaData.get_fileType());
					}
					if(metaData.get_lastModified() != null) {
						dbObject.put(CommonConstants.FILE_LAST_MODIFIED, metaData.get_lastModified());
					}
					if(metaData.get_owner() != null) {
						dbObject.put(CommonConstants.FILE_OWNER, metaData.get_owner());
					}
					filesFromDB.addAll(gfs.find(dbObject));

				} else {
					filesFromDB.addAll(gfs.find(metaData.get_filename()));
				}
			}

			System.out.println("Successfully retrived document from DB ...");
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
