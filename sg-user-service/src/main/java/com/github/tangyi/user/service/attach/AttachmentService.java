package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.service.IAttachmentService;
import com.github.tangyi.common.service.CrudService;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.mapper.attach.AttachmentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class AttachmentService extends CrudService<AttachmentMapper, Attachment> implements IAttachmentService {

	@Override
	@Cacheable(value = UserCacheName.ATTACHMENT, key = "#id")
	public Attachment get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.ATTACHMENT, key = "#attachment.id")
	public int update(Attachment attachment) {
		return super.update(attachment);
	}

	@Override
	@Transactional
	@CacheEvict(value = {UserCacheName.ATTACHMENT, UserCacheName.ATTACHMENT_URL}, key = "#attachment.id")
	public int delete(Attachment attachment) {
		return super.delete(attachment);
	}

	@Override
	@Transactional
	@CacheEvict(value = UserCacheName.ATTACHMENT, allEntries = true)
	public int deleteAll(Long[] ids) {
		return super.deleteAll(ids);
	}
}
