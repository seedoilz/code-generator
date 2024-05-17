package com.seedoilz.maker.generator.main;

import freemarker.template.TemplateException;
import java.io.*;

public class MainGenerator extends GeneratorTemplate{
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        new MainGenerator().doGenerate();
    }
}
