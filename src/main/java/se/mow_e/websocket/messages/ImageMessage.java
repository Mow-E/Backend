package se.mow_e.websocket.messages;


import java.nio.ByteBuffer;

public class ImageMessage {

    private Long id;

    private ByteBuffer data;

    private Integer chunkAmount;

    private Integer chunkOffset;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ByteBuffer getData() {
        return data;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }

    public Integer getChunkAmount() {
        return chunkAmount;
    }

    public void setChunkAmount(Integer chunkAmount) {
        this.chunkAmount = chunkAmount;
    }

    public Integer getChunkOffset() {
        return chunkOffset;
    }

    public void setChunkOffset(Integer offset) {
        this.chunkOffset = offset;
    }
}
