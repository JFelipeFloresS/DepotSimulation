/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.dataAccess;

import java.io.File;

/**
 *
 * @author josef
 */
public class DataSource {
    
    private static final DataSource instance = new DataSource();
    
    private static String path;
    
    /**
     * Data Source constructor
     * instantiate txtsimulationdao
     */
    private DataSource() {
    }

    /**
     * Sets current path for data access
     * @param p path
     * @return is existing file
     */
    public boolean setPath(String p) {
        path = p;
        File file = new File(p);
        return file.exists();
    }

    /**
     * @return static instance of datasource
     */
    public static DataSource getInstance() {
        return instance;
    }
    
    /**
     * @return current path of file
     */
    public String getPath() {
        return path;
    }
    
}
