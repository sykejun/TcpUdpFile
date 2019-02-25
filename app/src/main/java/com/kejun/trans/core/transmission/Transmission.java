package com.kejun.trans.core.transmission;

import java.io.Serializable;

public class Transmission implements Serializable {

    private int id;
    private int type;
    private String fileName;
    private String message;
    private long length;
    private long time;
    private int userId;
    private String savePath;
    private String sendPath;
    private int status;
    private int sr;

    public Transmission(int type,String fileName,String message,long length,
                        long time,int userId,String savePath,String sendPath,
                        int status,int sr)
    {
        this.type=type;
        this.fileName=fileName;
        this.message=message;
        this.length=length;
        this.time=time;
        this.userId=userId;
        this.savePath=savePath;
        this.sendPath=sendPath;
        this.status=status;
        this.sr=sr;
    }

    public Transmission(String fileName, int userId, int status) {
        this.fileName = fileName;
        this.status = status;
        this.userId = userId;
    }
    public Transmission(String fileName,int userId,int status,String savePath,int type,int sr)
    {
        this.fileName=fileName;
        this.status=status;
        this.userId=userId;
        this.savePath=savePath;
        this.type=type;
        this.sr=sr;
    }
    public Transmission()
    {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getSendPath() {
        return sendPath;
    }

    public void setSendPath(String sendPath) {
        this.sendPath = sendPath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSr() {
        return sr;
    }

    public void setSr(int sr) {
        this.sr = sr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
