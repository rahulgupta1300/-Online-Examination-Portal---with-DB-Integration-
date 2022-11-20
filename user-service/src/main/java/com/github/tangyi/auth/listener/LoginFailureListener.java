package com.github.tangyi.auth.listener;

import com.github.tangyi.auth.security.core.event.CustomAuthenticationFailureEvent;
import com.github.tangyi.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 *
 * 处理登录失败事件
 *
 * @author tangyi
 * @date 2019-11-11 23:52
 */
@Slf4j
@AllArgsConstructor
@Component
public class LoginFailureListener implements ApplicationListener<CustomAuthenticationFailureEvent> {

	private final UserService userService;

	@Override
	public void onApplicationEvent(CustomAuthenticationFailureEvent event) {
		// 登录失败后的处理

	}
}