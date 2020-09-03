package org.thinkingingis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thinkingingis.dto.TaxDTO;
import org.thinkingingis.service.TaxService;
import org.thinkingingis.utils.QRCodeUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/tax")
@Controller
public class TaxController {
    Logger logger = LoggerFactory.getLogger(TaxController.class);
    @Autowired
    private TaxService taxService;
    @GetMapping("/initQrcode")
    public void initQrcode(HttpServletResponse response, Model model) throws Exception {
        TaxDTO taxDTO = taxService.getFirstTax();
        String content = taxService.genContent(taxDTO);
        QRCodeUtils.createEncode(content, response.getOutputStream());

    }


    @GetMapping("/createQrCode")
    public void createQrCode(Long taxPk, HttpServletResponse response) throws Exception {
        TaxDTO taxDTO = taxService.getByPk(taxPk);
        String content = taxService.genContent(taxDTO);
        QRCodeUtils.createEncode(content, response.getOutputStream());
    }

    @GetMapping("/list")
    public HttpEntity<List<TaxDTO>> getTaxList(TaxDTO taxDTO) {
        return new HttpEntity<>(taxService.getList(taxDTO));
    }
    @GetMapping("/markUsedAndNext")
    public HttpEntity<TaxDTO> markUsedAndNext(Long taxPk) {
        return new HttpEntity<>(taxService.markUsedAndGetNext(taxPk));
    }
}
