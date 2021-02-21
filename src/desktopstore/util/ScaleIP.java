/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore.util;

/**
 *
 * @author aranda
 */
public class ScaleIP {
    
    private Scale scale;
    private String ip;
    
    public ScaleIP(String ip, Scale scale)
    {
        this.scale = scale;
        this.ip = ip;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
}
