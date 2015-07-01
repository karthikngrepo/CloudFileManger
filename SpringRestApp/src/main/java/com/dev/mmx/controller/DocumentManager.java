package com.dev.mmx.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dev.mmx.domain.beans.input.FileMetaData;
import com.dev.mmx.domain.constant.CommonConstants;
import com.dev.mmx.domain.dao.DocumentDAO;
import com.dev.mmx.domain.util.CommonUtilities;
import com.dev.mmx.domain.util.DAOUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * 
 * @author Karthik N G
 *
 */
@RestController
@RequestMapping(value= "/cloud")
public class DocumentManager {
	
	private Logger log = Logger.getLogger(DocumentManager.class);
	
	private static final String STATUS_SUCCESS = "SAVE SUCCESS";
	
	private static final String STATUS_FAIL = "SAVE FAILED";
	
	@Autowired
	private DocumentDAO documentDAO;
	
	@RequestMapping(value="/documents/upload", method=RequestMethod.POST)
	public String upload(HttpServletRequest request) throws Exception {
		
		documentDAO.saveDocumentToDB(request);
		
		return STATUS_SUCCESS;
	}
	
	@RequestMapping(value="/documents/metainfo", method=RequestMethod.GET)
	public List<FileMetaData> getDocumentMetaInfo(HttpServletRequest request) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
    	List<FileMetaData> metaDataList = new ArrayList<FileMetaData>();
    	
    	Path filepath = Paths.get(CommonUtilities.getMetaData(request, CommonConstants.FILE_NAME));
    	List<GridFSDBFile> documents = documentDAO.getDocuments(filepath);
    	
    	for (GridFSDBFile gridFSDBFile : documents) {
    		FileMetaData metaDataFromDB = new FileMetaData();
    		if(gridFSDBFile.getFilename() != null) {
    			metaDataFromDB.set_filename(gridFSDBFile.getFilename());
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_ACCESSED) != null) {
    				metaDataFromDB.set_accessed(DAOUtil.sdf.format(gridFSDBFile.getMetaData().get(CommonConstants.FILE_ACCESSED)));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_CONTENTTYPE) != null) {
    				metaDataFromDB.set_contentType((String)gridFSDBFile.getMetaData().get(CommonConstants.FILE_CONTENTTYPE));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_CREATED) != null) {
    				metaDataFromDB.set_created(DAOUtil.sdf.format(gridFSDBFile.getMetaData().get(CommonConstants.FILE_CREATED)));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_SIZE) != null) {
    				metaDataFromDB.set_fileSize((String)gridFSDBFile.getMetaData().get(CommonConstants.FILE_SIZE));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_TYPE) != null) {
    				metaDataFromDB.set_fileType((String)gridFSDBFile.getMetaData().get(CommonConstants.FILE_TYPE));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_LAST_MODIFIED) != null) {
    				metaDataFromDB.set_lastModified(DAOUtil.sdf.format(gridFSDBFile.getMetaData().get(CommonConstants.FILE_LAST_MODIFIED)));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_OWNER) != null) {
    				metaDataFromDB.set_owner((String)gridFSDBFile.getMetaData().get(CommonConstants.FILE_OWNER));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.RECORD_CREATED) != null) {
    				metaDataFromDB.set_owner(DAOUtil.sdf.format(gridFSDBFile.getMetaData().get(CommonConstants.RECORD_CREATED)));
    			}
    		}
    		metaDataList.add(metaDataFromDB);
		}
    	return metaDataList;
	}
}