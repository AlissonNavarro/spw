package ClockManager;

import java.io.Serializable;

public class Rep implements Serializable {

    String ip;
    boolean resultado;
    int i;
    String comentario;

    Rep() {
        ip = "";
        resultado = false;
        i = -1;
        comentario = "?";
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isResultado() {
        return resultado;
    }

    public void setResultado(boolean resultado) {
        this.resultado = resultado;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    
    
}