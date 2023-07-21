package com.dino.common.constants;

/**
 * @author Zhang Jinming
 * @date 7/5/2023 2:47 PM
 */
public class OpenAiConst {
    public static final Long GPT_ID = -10101010L;

    public static final String END_FLAG = "[DONE]";

    public static final String TITLE_PROMPT = "你是一个标题生成器,为这句话（可能很短）生成尽可能简洁的标题,10个字以内越短越好,语言由这段话的主要语言决定：";

    public static final String ASSISTANCE_PROMPT = "我们的软件叫做DINO OFFICE，是一个OA系统，现在你充当我们系统的AI工作助理，帮助我们的客户解决一些问题，以下是客户的与你的对话，你回复他的问题，然后他会继续提问，直到他满意为止。语言由他提问时的主要语言决定：";
}
