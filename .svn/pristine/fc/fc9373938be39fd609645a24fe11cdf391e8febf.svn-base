package nurier.scraping.common.service;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class FileAccessService {
    private String  defaultEncoding = "UTF-8";
    private boolean defaultReadFileType = false; 

    private File    file = null;  
    private String  filePath = null;
    private String  fileName = null;

    private boolean readFileType = false;
    
    private String  readString = null;
    private boolean nextFlag = true;
    
    private BufferedWriter bw = null;
    private BufferedReader br = null;
    
    /**
     * 파일 불러오기 생성자 (읽기/쓰기)
     * @param filePath              접근 파일 경로
     * @param fileName              접근 파일명
     */
    public FileAccessService(String filePath, String fileName) {
        setFileAccessService(filePath, fileName, this.defaultReadFileType, this.defaultEncoding);
    }
    /**
     * 파일 불러오기 생성자 (읽기/쓰기)
     * @param filePath              접근 파일 경로
     * @param fileName              접근 파일명
     * @param defaultEncoding       파일 인코딩
     */
    public FileAccessService(String filePath, String fileName, String defaultEncoding) {
        setFileAccessService(filePath, fileName, this.defaultReadFileType, defaultEncoding);
    }
    /**
     * 파일 불러오기 생성자 (읽기/쓰기)
     * @param filePath              접근 파일 경로
     * @param fileName              접근 파일명
     * @param defaultReadFileType   쓰기구분(false:새로 쓰기, true:이어서 쓰기)
     */
    public FileAccessService(String filePath, String fileName, boolean defaultReadFileType) {
        setFileAccessService(filePath, fileName, defaultReadFileType, this.defaultEncoding);
    }
    /**
     * 파일 불러오기 생성자 (읽기/쓰기)
     * @param filePath              접근 파일 경로
     * @param fileName              접근 파일명
     * @param defaultReadFileType   쓰기구분(false:새로 쓰기, true:이어서 쓰기)
     * @param defaultEncoding       파일 인코딩
     */
    public FileAccessService(String filePath, String fileName, boolean defaultReadFileType, String defaultEncoding) {
        setFileAccessService(filePath, fileName, defaultReadFileType, defaultEncoding);
    }
    private void setFileAccessService(String filePath, String fileName, boolean defaultReadFileType, String defaultEncoding) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.defaultReadFileType = defaultReadFileType;
        this.defaultEncoding = defaultEncoding;
        
        getFileObject();
    }
    
    //public void write(String text)                  {   record(1, text, false);     }
    //public void writeLine(String text)              {   record(1, text, true);      }
    public void write(String[] text)                {   record(1, text, false);     }
    public void writeLine(String[] text)            {   record(1, text, true);      }
    public void write(ArrayList<String> text)       {   record(1, text, false);     }
    public void writeLine(ArrayList<String> text)   {   record(1, text, true);      }
    //public void append(String text)                 {   record(2, text, false);     }
    //public void appendLine(String text)             {   record(2, text, true);      }
    public void append(String[] text)               {   record(2, text, false);     }
    public void appendLine(String[] text)           {   record(2, text, true);      }
    public void append(ArrayList<String> text)      {   record(2, text, false);     }
    public void appendLine(ArrayList<String> text)  {   record(2, text, true);      }

    /**
     * 파일에 문자열 쓰기 
     * @param type      1:write 2:append      
     * @param text
     * @param newLine   true/false
     */
    private void record(int type, String[] text, boolean newLine) {
        ArrayList<String> arrayText = new ArrayList<String>(text.length);
        for(String s : text) { arrayText.add(s); }
        record(type, arrayText, newLine);
    }
    
    private void record(int type, ArrayList<String> arrayText, boolean newLine) {
        try {
            if (this.file == null) {
                getFileObject();
            }
            
            if ( bw == null || ( bw != null && readFileType != defaultReadFileType )) {
                defaultReadFileType = readFileType;
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file, defaultReadFileType), defaultEncoding));
            }
            
            for (String text : arrayText) {
                if (type == 1) {
                    bw.write(text);
                } else if (type == 2){
                    bw.append(text);
                }
                if (newLine) bw.newLine();
            }
            setFilepermission(false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ( this.bw != null) {
                try {
                    this.bw.flush();
                    this.bw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 파일에서 한줄식 문자열을 읽어옴
     * @return
     */
    public boolean read() {
        try {
            if (this.file == null) {
                getFileObject();
            }
            
            if ( br == null ) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file), defaultEncoding));
            }
            
            if ( nextFlag ) {
                readString = br.readLine();

                if ( readString == null || readString.trim().length() == 0 ) {
                    readString = "";
                    nextFlag = false;
                    
                    readClose();
                    return false;
                } else {
                    return true;
                }
            } else {
                readClose();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            
            //Exception 발생시 Object Close
            readClose();
        } catch (Exception e) {
            e.printStackTrace();

            //Exception 발생시 Object Close
            readClose();
        }
        readClose();
        return false;
    }
    
    /**
     * 파일 존재 확인
     * @return
     */
    public boolean isFile() {
        if (file == null ) {
            return false;
        } else {
            return file.isFile();
        }
    }
    
    public boolean deleteFile() {
        if (file == null ) {
            return false;
        } else {
            return file.delete();
        }
    }

    /**
     * 업로드 파일의 권한 설정
     * 실행권한을 모두 제거하고 Other까지 읽기 권한 설정
     * @param File 화면으로부터 업로드된 File객체
     * @param isPerm true:WAS계정만 읽기권한, false:Other까지 읽기권한 설정
     * @throws Exception File 권한변경 실패
     */
    public void setFilepermission(boolean isPerm) {
        if (this.file != null) {
            // 실행권한 제거(전체-소유자, 그룹, Other)
            this.file.setExecutable(false, false);
            // 읽기권한 부여(NAS 사용-소유자, 그룹, Other)
            this.file.setReadable(true, isPerm);
        }
    }
    
    /**
     * 배열로 구성된 문자 배열을 구분자를 추가하면서 하나의 문자열로 변환
     * @param arrString     문자 배열
     * @param ch            구분문자
     * @return
     */
    public String getStringMarge(String[] arrString, String ch) {
        StringBuffer returnString = new StringBuffer(50);
        if ( arrString != null && arrString.length != 0 ) {
            for ( int i=0; i<arrString.length; i++ ) {
                returnString.append(arrString[i]);
                if ( i < arrString.length ) returnString.append(ch);
            }
        }
        return returnString.toString();
    }

    public void writeEnd() {
        try {
            if ( this.bw != null) {
                this.bw.flush();
                this.bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readClose() {
        try {
            if ( this.br != null) {
                this.br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 파일 쓰기 구분 셋팅 
     * @param readFileType     쓰기구분(false:새로 쓰기, true:이어서 쓰기)
     */
    public void setReadFileType(boolean readFileType) {
        this.readFileType = readFileType;
    }

    private void getFileObject() {
        if ( !StringUtils.isBlank(this.fileName) && !StringUtils.isBlank(this.filePath)) {
            this.file = new File(filePath + fileName);
        }
    }
    
    public String getReadString() {
        return readString;
    }
}

