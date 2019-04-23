package com.wangdinghua.springbootdemo.springbootdemo.springTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TemplateDownLoadGetAPI {
	private TestRestTemplate template = null;

    @Before
    public void testBefore() {
        template = new TestRestTemplate();
    }

    @Test
    public void testGet() {
    	long start = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Resource> httpEntity = new HttpEntity<Resource>(headers);
//        ResponseEntity<byte[]> response = template.exchange("http://172.16.0.6:8080/one-infrastructure-api/api/v1/files/05fa7dcd-7b2a-4e93-8450-92bd332266ee/types/file", HttpMethod.GET,
		ResponseEntity<byte[]> response = template.exchange("http://172.16.0.6:8080/one-infrastructure-api/api/v1/files/7b677a90-466f-43da-8fa4-3fd8d51d541b/types/file", HttpMethod.GET,
                httpEntity, byte[].class);
        log.info("===状态码================");
        log.info(">> {}", response.getStatusCodeValue());
        log.info("===返回信息================");
        log.info(">> {}", response.getHeaders().getContentType());
        log.info(">> {}", response.getHeaders().getContentType().getSubtype());
        try {
            File file = File.createTempFile("ess-", "." + response.getHeaders().getContentType().getSubtype());
			String directory = "E:\\Temp";
			String filename = response.getHeaders().get("filename").get(0);
			file = new File(directory , filename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(response.getBody());
            fos.flush();
            fos.close();
            long end = System.currentTimeMillis();
    		System.err.println("use time ===>>>" + (end - start));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void test1(){
    	long start = System.currentTimeMillis();
    	RequestCallback requestCallback = request -> request.getHeaders()
                .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));

        // Streams the response instead of loading it all in memory
        ResponseExtractor<Void> responseExtractor = response -> {
            //write content to the destination file
            Files.copy(response.getBody(), new File("E:\\Temp",response.getHeaders().get("filename").get(0)).toPath());
            return null;
        };

        String requestURL = "http://172.16.0.6:8080/one-infrastructure-api/api/v1/files/05fa7dcd-7b2a-4e93-8450-92bd332266ee/types/file";
		template.execute(URI.create(requestURL ), HttpMethod.GET, requestCallback, responseExtractor);
		long end = System.currentTimeMillis();
		System.err.println("use time ===>>>" + (end - start));
    }
    
    @After
    public void testAfter() {
        template = null;
    }

}
