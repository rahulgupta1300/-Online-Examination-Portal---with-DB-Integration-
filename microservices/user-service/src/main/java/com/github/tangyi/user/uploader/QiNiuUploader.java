package com.github.tangyi.user.uploader;

import com.github.tangyi.api.user.module.Attachment;
import com.github.tangyi.common.oss.service.QiNiuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * 上传到七牛云
 *
 * @author tangyi
 * @date 2020/04/05 13:36
 */
@Slf4j
@Service
public class QiNiuUploader extends AbstractUploader {

    @Override
    public Attachment upload(Attachment attachment, byte[] bytes) {
        String result = QiNiuUtil.getInstance().upload(bytes, attachment.getAttachName());
        attachment.setUploadResult(result);
        attachment.setPreviewUrl(this.getDownloadUrl(attachment, -1));
        return attachment;
    }

    @Override
    public InputStream download(Attachment attachment) {
        return null;
    }

    @Override
    public String getDownloadUrl(Attachment attachment) {
        try {
            return QiNiuUtil.getInstance().getDownloadUrl(attachment.getAttachName());
        } catch (Exception e) {
            log.error("getDownloadUrl failed", e);
            return "";
        }
    }

    @Override
    public String getDownloadUrl(Attachment attachment, int expire) {
        try {
            return QiNiuUtil.getInstance().getDownloadUrl(attachment.getAttachName(), expire);
        } catch (Exception e) {
            log.error("getDownloadUrl failed", e);
            return "";
        }
    }

    @Override
    public boolean delete(Attachment attachment) {
        return QiNiuUtil.getInstance().delete(attachment.getAttachName());
    }

    @Override
    public boolean deleteAll(Attachment attachment) {
        return false;
    }
}
