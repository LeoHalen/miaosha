package site.zgcoding.miaosha.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.zgcoding.miaosha.model.LoginRequest;
import site.zgcoding.miaosha.vo.LoginVo;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Author Zg.Li · 2020/4/20
 */
@Slf4j
@Controller
public class LoginController {


	@GetMapping("/doLogin")
	@ResponseBody
	public void doLogin(@Valid LoginVo loginVo) {



	}


}
