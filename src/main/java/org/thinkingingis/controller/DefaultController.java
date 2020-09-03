package org.thinkingingis.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thinkingingis.dto.TaxDTO;
import org.thinkingingis.service.TaxService;
import org.thinkingingis.utils.QRCodeGenerator;

import com.google.zxing.WriterException;

@Controller
public class DefaultController {
    @Autowired
    private TaxService taxService;
	@GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }
    
    @GetMapping("qrcode")
    public String qrcode() {
    	return "qrcode";
    }

    @GetMapping("/taxQrcode")
    public String taxQrcode(Model model) {
        TaxDTO taxDTO = taxService.getFirstTax();
        model.addAttribute("tax",taxDTO);
        return "taxQrcode";
    }

    @GetMapping(value="/qrimage")
	public ResponseEntity<byte[]> getQRImage() {
		
		//二维码内的信息
		String info = "This is my first QR Code";
		
		byte[] qrcode = null;
		try {
			qrcode = QRCodeGenerator.getQRCodeImage(info, 360, 360);
		} catch (WriterException e) {
			System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			
			System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
		} 

	    // Set headers
	    final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);

	    return new ResponseEntity<byte[]> (qrcode, headers, HttpStatus.CREATED);
	}
    
    
    
}
