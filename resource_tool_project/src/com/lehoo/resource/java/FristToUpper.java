package com.lehoo.resource.java;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class FristToUpper implements TemplateDirectiveModel {

    public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (!params.isEmpty()) {
            throw new TemplateModelException(
                    "This directive doesn't allow parameters.");
        }
        if (loopVars.length != 0) {
                throw new TemplateModelException(
                    "This directive doesn't allow loop variables.");
        }
        
        if (body != null) {
            body.render(new UpperCaseFilterWriter(env.getOut()));
        } else {
            throw new RuntimeException("missing body");
        }
    }
    
    private static class UpperCaseFilterWriter extends Writer {
       
        private final Writer out;
           
        UpperCaseFilterWriter (Writer out) {
            this.out = out;
        }

        public void write(char[] cbuf, int off, int len)
                throws IOException {
        	cbuf[0] = Character.toUpperCase(cbuf[0]);
        	out.write(String.valueOf(cbuf).trim());///把右边空格去掉
        }

        public void flush() throws IOException {
            out.flush();
        }

        public void close() throws IOException {
            out.close();
        }
    }

}