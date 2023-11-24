package com.example.myapp_20;

public class WriteInfo
{
    private String WriterUid;
    private String Writer;
    private String title;
    private String group;
    private String detail;
    private String post;
    private int curPeople;
    private int maxPeople;
    private String time;

    private String writeId;
    private String groupId;


    public WriteInfo()
    {

    }

    public String getWriteId() {
        return writeId;
    }

    public void setWriteId(String writeId) {
        this.writeId = writeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getCurPeople() {
        return curPeople;
    }

    public void setCurPeople(int curPeople) {
        this.curPeople = curPeople;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }


    public String getWriterUid() {
        return WriterUid;
    }

    public void setWriterUid(String writerUid) {
        WriterUid = writerUid;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
