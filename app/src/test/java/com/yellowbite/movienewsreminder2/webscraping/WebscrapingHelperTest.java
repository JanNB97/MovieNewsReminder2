package com.yellowbite.movienewsreminder2.webscraping;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

public class WebscrapingHelperTest
{
    @Test
    public void testGetDoc()
    {
        try
        {
            Assert.assertNotNull(WebscrapingHelper.getDoc("https://opac.winbiap.net/mzhr/detail.aspx?data=U29ydD1adWdhbmdzZGF0dW0gKEJpYmxpb3RoZWspJmFtcDtzQz1jXzA9MSUlbV8wPTElJWZfMD02MyUlb18wPTYlJXZfMD0yNS4wOC4yMDE2IDAwOjAwOjAwKytjXzE9MSUlbV8xPTElJWZfMT00MiUlb18xPTElJXZfMT00NlMtRFZEIChTcGllbGZpbG0pKytjXzI9MSUlbV8yPTElJWZfMj00OCUlb18yPTElJXZfMj1NZWRpZW56ZW50cnVtIEhlcnNmZWxkLVJvdGVuYnVyZyZhbXA7Y21kPTEmYW1wO0NhdGFsb2d1ZUlkPTE5MDg5OCZhbXA7cGFnZUlkPTMmYW1wO1NyYz0zJmFtcDtwUz0xMA==-wjfxjVJjqng="));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
