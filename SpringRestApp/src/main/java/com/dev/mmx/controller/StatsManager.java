package com.dev.mmx.controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping(value= "/filemanger")
public class StatsManager {
	
	private static final String STATUS_SUCCESS = "SAVE SUCCESS";
	
	private static final String STATUS_FAIL = "SAVE FAILED";
	
	@Autowired
	private DocumentDAO documentDAO;
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String storeFileInDB(@RequestParam(value="location", required=true) String location, 
			@RequestParam(value="filename", required=true) String filename) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
    	
		FileMetaData metaData = CommonUtilities.getFileMetaInfo(Paths.get("./"+filename));
		
		documentDAO.saveDocument(metaData, Paths.get("./"+filename));
		
		return STATUS_SUCCESS;
	}
	
	@RequestMapping(value="/pulldocumentmetainfo", method=RequestMethod.GET)
	public List<FileMetaData> getDocumentMetaInfo(@RequestParam(value="metaData", required=true) String metaData) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
    	FileMetaData[] fileMetaData = mapper.readValue(metaData, FileMetaData[].class);
    	List<FileMetaData> metaDataList = new ArrayList<FileMetaData>();
    	
    	List<GridFSDBFile> documents = documentDAO.getDocumentMetadata(fileMetaData);
    	
    	for (GridFSDBFile gridFSDBFile : documents) {
    		FileMetaData metaDataFromDB = new FileMetaData();
    		if(gridFSDBFile.getFilename() != null) {
    			metaDataFromDB.set_filename(gridFSDBFile.getFilename());
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_ACCESSED) != null) {
    				metaDataFromDB.set_accessed((String)gridFSDBFile.getMetaData().get(CommonConstants.FILE_ACCESSED));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_CONTENTTYPE) != null) {
    				metaDataFromDB.set_contentType((String)gridFSDBFile.getMetaData().get(CommonConstants.FILE_CONTENTTYPE));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_CREATED) != null) {
    				metaDataFromDB.set_created((String)gridFSDBFile.getMetaData().get(CommonConstants.FILE_CREATED));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_SIZE) != null) {
    				metaDataFromDB.set_fileSize((String)gridFSDBFile.getMetaData().get(CommonConstants.FILE_SIZE));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_TYPE) != null) {
    				metaDataFromDB.set_fileType((String)gridFSDBFile.getMetaData().get(CommonConstants.FILE_TYPE));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_LAST_MODIFIED) != null) {
    				metaDataFromDB.set_lastModified((String)gridFSDBFile.getMetaData().get(CommonConstants.FILE_LAST_MODIFIED));
    			}
    			if(gridFSDBFile.getMetaData().get(CommonConstants.FILE_OWNER) != null) {
    				metaDataFromDB.set_owner((String)gridFSDBFile.getMetaData().get(CommonConstants.FILE_OWNER));
    			}
    		}
    		metaDataList.add(metaDataFromDB);
		}
    	return metaDataList;
	}
}