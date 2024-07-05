package clases;

import java.awt.Font;
import javax.swing.JLabel;

public class Tiempo extends Thread {

    private int segundos = 0;
    private JLabel relojLabel;
    private boolean enEjecucion;
    
   public Tiempo(JLabel reloj){
        this.relojLabel=reloj;
        this.enEjecucion=true;
    }

    @Override
    public  void run() {
        while (enEjecucion) {
            
       if(segundos>999){
           relojLabel.setFont(new Font("leelawadee", 0, 36));
       }
       relojLabel.setText(Integer.toString(segundos++));
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                System.out.print("error en la Clase Tiempo " + e);
            }
        }
    }
    public void detener(){
        enEjecucion=false;
    }
    public int getSegundos() {
        return segundos;
    }
    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }
}
