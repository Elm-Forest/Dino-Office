package com.dino.common.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件
 *
 * @author CTGU_LLZ(404name)
 * @date 2021/08/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailBO {

    /**
     * 邮箱号
     */
    private String email;

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

}
