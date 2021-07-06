package com.clay.gulimall.product.vo;

import com.clay.gulimall.product.dto.AttrDTO;
import lombok.Data;

import java.util.ArrayList;

@Data
public class AttrRespVO extends AttrDTO {

    private String catlogName;


    private String groupName;

    private ArrayList<Long> catlogPath;

}
