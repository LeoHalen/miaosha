package site.zgcoding.miaosha.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.zgcoding.miaosha.model.LoginRequest;
import site.zgcoding.miaosha.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Author Zg.Li · 2020/4/20
 */
@Slf4j
@Service
public class AuthService {

	public boolean login(HttpServletResponse response , LoginVo loginVo) {
		if(loginVo ==null){
			throw  new GlobleException(SYSTEM_ERROR);
		}

		String mobile =loginVo.getMobile();
		String password =loginVo.getPassword();
		MiaoshaUser user = getByNickName(mobile);
		if(user == null) {
			throw new GlobleException(MOBILE_NOT_EXIST);
		}

		String dbPass = user.getPassword();
		String saltDb = user.getSalt();
		String calcPass = MD5Utils.formPassToDBPass(password,saltDb);
		if(!calcPass.equals(dbPass)){
			throw new GlobleException(PASSWORD_ERROR);
		}
		//生成cookie 将session返回游览器 分布式session
		String token= UUIDUtil.uuid();
		addCookie(response, token, user);
		return true ;
	}

	public LoginResponse login(HttpServletResponse response , LoginVo loginVo) {
		String mobile =loginVo.getMobile();
		String password =loginVo.getPassword();
		try {
			loginRequest.setPassword(URLDecoder.decode(loginRequest.getPassword(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		try {
			Account account = userService.validate(loginRequest.getUsername(), loginRequest.getPassword());
//            String random = RandomUtil.getRandom(keyConfiguration.getRandomKey());
//            if (!"jvXu4N3c6aeKRhIJ".equals(loginRequest.getRandom())) {
//                if (!random.equals(loginRequest.getRandom())) {
//                    log.debug("尝试登录的随机数: {}", loginRequest.getUsername());
//                    return new LoginResponse(LoginResponse.STATUS_ERROR_RANDOM);
//                }
//            }

			String token = tokenService.generateToken(account.toInfo());
			LoginResponse loginResponse = new LoginResponse(LoginResponse.STATUS_LOGIN_SUCCESS, token,
					account.getRightRange());
			loginResponse.setUserType(account.getType());
			loginResponse.setName(account.getName());
			return loginResponse;
		} catch (UnknownAccountException | IncorrectCredentialsException ex) {
			if (ex instanceof UnknownAccountException) {
				log.debug("尝试登录的帐号未知: {}", loginRequest.getUsername());
			} else {
				log.debug("尝试登录的帐号密码错误: {}", loginRequest.getUsername());
			}
			// 密码错误/帐号错误, 均返回帐号未知的状态
			return new LoginResponse(LoginResponse.STATUS_UNKNOWN_ACCOUNT);
		} catch (LockedAccountException ex) {
			log.debug("尝试登录的帐号已被锁定: {}, 锁定原因: 尝试登录次数超过允许次数: {}", loginRequest.getUsername(), ex.getAttemptLoginCount());
			return new LoginResponse(LoginResponse.STATUS_ACCOUNT_LOCKED)
					.setAttemptLoginCount(ex.getAttemptLoginCount());
		} catch (DisabledAccountException ex) {
			log.debug("尝试登录的帐号已被禁用: {}", loginRequest.getUsername());
			return new LoginResponse(LoginResponse.STATUS_DISABLED_ACCOUNT);
		}
	}
}
