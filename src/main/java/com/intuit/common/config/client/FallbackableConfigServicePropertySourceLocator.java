package com.intuit.sbg.common.config.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: asachdeva
 * Date: 7/6/15
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Order(0)
public class FallbackableConfigServicePropertySourceLocator extends ConfigServicePropertySourceLocator {

    private boolean fallbackEnabled;
    private String fallbackLocation;

    @Autowired(required = false)
    TextEncryptor textEncryptor;

    public FallbackableConfigServicePropertySourceLocator(ConfigClientProperties defaults, String fallbackLocation) {
        super(defaults);
        this.fallbackLocation = fallbackLocation;
        fallbackEnabled = !StringUtils.isEmpty(fallbackLocation);
    }

    public PropertySource<?> locate(Environment environment) {
        PropertySource<?> propertySource = super.locate(environment);
        if(fallbackEnabled){
            if(propertySource!=null){
                storeLocally(propertySource);
            }
        }
        return propertySource;
    }

    private void storeLocally(PropertySource propertySource){
        StringBuilder b = new StringBuilder();
        CompositePropertySource composite = (CompositePropertySource) propertySource;
        for(String propertyName: composite.getPropertyNames()){
            Object value = composite.getProperty(propertyName);
            if(textEncryptor!=null)
                value = "{cipher}"+textEncryptor.encrypt(String.valueOf(value));
            b.append(propertyName+"="+value+"\n");
        }
        saveFile(b.toString());
    }

    private void saveFile(String contents){
        BufferedWriter output = null;
        try {
            File file = new File(fallbackLocation+File.separator+ ConfigServiceBootstrap.FALLBACK_FILE_NAME);
            output = new BufferedWriter(new FileWriter(file));
            output.write(contents);
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( output != null )
                try{
                    output.close();
                }catch (Exception e){
                    System.out.print("Error"+e.getMessage());
                }
        }
    }
}
