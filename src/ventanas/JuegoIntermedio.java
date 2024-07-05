
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import clases.Tiempo;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author Rodri
 */
public class JuegoIntermedio extends JFrame implements MouseListener {

    private JButton[][] botones;
    private JLabel[][] minas;
    private ImageIcon tapaMina;
    private ImageIcon botonMinaApretado;
    private ImageIcon imgMina;
    private final ImageIcon imgBandera;
    private final ImageIcon imgMinaExplotada;
    private final ImageIcon caritaAsombradaBoton;
    private final ImageIcon caritaFelizBoton;
    private final ImageIcon caritaTristeBoton;
    private final int cantidadMinas;
    private int x;
    private int y;
    private int contadorInicio = 0;
    public int segundos;
    public boolean juegoTerminado;
    private static Tiempo tiempo;

    public JLabel getLabelSegundos() {
        return labelSegundos;
    }

    public void setLabelSegundos(JLabel labelSegundos) {
        this.labelSegundos = labelSegundos;
    }

    /**
     * Creates new form JuegoIntermedio
     */
    public JuegoIntermedio() {
        cantidadMinas = 50;//esta variable determina el numero de JLabel que tendrán la imagen de la mina
        imgMina = new ImageIcon(getClass().getResource("/imagenes/mina.png"));
        imgMinaExplotada = new ImageIcon(getClass().getResource("/imagenes/minaExplotada.png"));
        imgBandera = new ImageIcon(getClass().getResource("/imagenes/tapaMinaBandera.png"));
        caritaFelizBoton = new ImageIcon(getClass().getResource("/imagenes/botonCarita.png"));
        caritaTristeBoton = new ImageIcon(getClass().getResource("/imagenes/botonCaritaTriste.png"));
        caritaAsombradaBoton = new ImageIcon(getClass().getResource("/imagenes/botonCaritaAsombrada.png"));
        initComponents();
        creadorDeBotones();
        creadorDeLabelsMina();
        creadorDeMinas();
        labelBanderas.setText(Integer.toString(cantidadMinas));
        this.setSize(686, 788);
        this.setLocationRelativeTo(null);
        this.setTitle("Buscaminas");
        this.setIconImage(imgMina.getImage());
        this.setBackground(Color.gray);
    }

    private void creadorDeBotones() {

        tapaMina = new ImageIcon(getClass().getResource("/imagenes/tapaMina.png"));
        botonMinaApretado = new ImageIcon(getClass().getResource("/imagenes/botonMinaApretado.png"));
        botones = new JButton[22][22];
        for (int i = 0; i < botones.length; i++) {
            for (int j = 0; j < botones.length; j++) {
                botones[i][j] = new JButton();
                botones[i][j].setIcon(tapaMina);
                botones[i][j].setPressedIcon(botonMinaApretado);
                botones[i][j].setBounds((29 * i) + 1, (29 * j) + 2, 28, 28);
                botones[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        botonMinaMouseClicked3(evt);
                    }

                    @Override
                    public void mousePressed(MouseEvent evt) {
                        botonMinaMousePressed(evt);
                    }

                    @Override
                    public void mouseReleased(MouseEvent evt) {
                        botonMinaMouseReleased(evt);
                    }
                });
                fondo.add(botones[i][j]);
            }
        }
    }

    private void creadorDeLabelsMina() {
        minas = new JLabel[22][22];//se inicializa el array de JLabel con las dimensiones correspondientes al nivel
        for (int i = 0; i < minas.length; i++) {//se recorren las filas del array
            for (int j = 0; j < minas.length; j++) {//se recorren las columnas del array
                minas[i][j] = new JLabel();//se declara un JLabel para la posicion del array correspondiente
                minas[i][j].setHorizontalAlignment(0);//se posiciona el texto al centro
                minas[i][j].setFont(new Font("arial", 1, 20));//se asigna un tipo de fuente "Arial", en negrita(1) con tamaño 20
                minas[i][j].setText("0");//se asigna un texto por defecto
                minas[i][j].setBounds((29 * i) + 1, (29 * j) + 2, 28, 28);//se ubica el objeto en la posición correcta con la fórmula que se vale
                //del valor de "i" y "j" y los multiplica por 29 que es el tamaño de cada JLabel más 1 pixel de separación, y a esto se añade 
                //+1 para "x" y +2 para "y" para acomodar el diseño, luego se dan las dimensiones 28px para ancho y altura
                fondo.add(minas[i][j]);//se añade el objeto creado al label "fondo que los contiene"
            }
        }
    }

    private void creadorDeMinas() {
        imgMina = new ImageIcon(getClass().getResource("/imagenes/mina.png"));//se inicializa el ImageIcon con la imagen de la mina
        int posX;//se declara la variable x que servirá para las coordenadas de ubicacion de las minas
        int posY;//se declara la variable x que servirá para las coordenadas de ubicacion de las minas
        for (int i = 0; i < cantidadMinas; i++) {//este bucle cargará las minas
            do {
                posX = (int) (Math.random() * minas.length);//crea un valor aleatorio multiplicado por la dimensión del array
                posY = (int) (Math.random() * minas.length);//para cada uno de los ejes
            } while (minas[posX][posY].getIcon() == imgMina);//lo repite mientras en la posicion generada ya haya sido colocada una mina
            minas[posX][posY].setIcon(imgMina);//carga la imagen de la mina en el JLabel
            minas[posX][posY].setText("");//cuando el JLabel contiene una mina se quita el texto puesto por defecto para que no interfiera con la imagen
            System.out.println("Hay una mina en:\n x=" + posX + ", y=" + posY);
        }
        System.out.println("cantidad de minas: " + cantidadMinas);//se muestra por consola la cantidad de minas generadas
        creadorDeNumeros();
    }

    private void creadorDeNumeros() {
        for (int i = 0; i < minas.length; i++) {
            for (int j = 0; j < minas.length; j++) {
                if (minas[i][j].getIcon() == imgMina) {
                    List posiciones = minasAlrededor(i, j);
                    for (int k = 0; k < posiciones.size(); k++) {
                        JLabel posicionObtenida = (JLabel) posiciones.get(k);
                        if (posicionObtenida.getIcon() != imgMina) {
                            posicionObtenida.setText(Integer.toString(Integer.parseInt(posicionObtenida.getText()) + 1));
                        }
                    }
                }
            }
        }
        for (JLabel[] mina : minas) {
            for (int j = 0; j < minas.length; j++) {
                switch (mina[j].getText()) {
                    case "8":
                        mina[j].setForeground(Color.orange);
                        break;
                    case "7":
                        mina[j].setForeground(Color.pink);
                        break;
                    case "6":
                        mina[j].setForeground(Color.CYAN);
                        break;
                    case "5":
                        mina[j].setForeground(Color.magenta);
                        break;
                    case "4":
                        mina[j].setForeground(Color.decode("#08026a"));
                        break;
                    case "3":
                        mina[j].setForeground(Color.red);
                        break;
                    case "2":
                        mina[j].setForeground(Color.green);
                        break;
                    case "1":
                        mina[j].setForeground(Color.blue);
                        break;
                    default:
                        mina[j].setForeground(Color.lightGray);
                        break;
                }
            }
        }
        fondo.repaint();
    }

    private void botonMinaMouseClicked3(MouseEvent evt) {
        JButton boton = (JButton) evt.getSource();//se crea un objeto JBuuton y se inicializa con el objeto fuente del evento
        x = (boton.getX() - 1) / 29;//se inicializan las variables "x" e "y" con los valores de la posicion respectiva a cada eje del
        y = (boton.getY() - 2) / 29;//objeto obtenido del evento y se aplica la fórmula inversa que devolverá la posición del objeto en la matriz
        System.out.println("Posición del objeto:\n x=" + boton.getX() + ", y=" + boton.getY());//se imprime por consola posicion del objeto en la pantalla
        System.out.println("Ubicación en la matriz:\n i=" + x + ", j=" + y);//se imprime por consola Ubicación en la matriz
        if (!juegoTerminado) {
            if (evt.getModifiers() == 4) {//elavlúa si se ha presionado el boton derecho del mouse
                System.out.println("Se presionó el click derecho");
                if (boton.getIcon() != imgBandera) {//evalúa si el botón tiene una bandera
                    System.out.println("El boton no tiene bandera");
                    if (Integer.parseInt(labelBanderas.getText()) > 0) {
                        boton.setIcon(imgBandera);//inserta la imagen de la bandera en el botón
                        labelBanderas.setText(Integer.toString(Integer.parseInt(labelBanderas.getText()) - 1));
                    }
                } else {//si el botón ya tiene bandera
                    System.out.println("El boton ya tenia una bandera");
                    boton.setIcon(tapaMina);//se reemplaza la bandera por la imagen original del botón
                    labelBanderas.setText(Integer.toString(Integer.parseInt(labelBanderas.getText()) + 1));
                }
            } else {
                if (contadorInicio == 0) {
                    System.out.println("Se presionó el click izquierdo por primera vez");
                    tiempo = new Tiempo(labelSegundos);
                    tiempo.start();
                    while (minas[x][y].getIcon() != null) {
                        System.out.println("La primera casilla seleccionada era una mina");
                        System.out.println("Se llamará al método resetearJuego()");
                        resetearJuego();
                    }
                    contadorInicio++;

                } else {
                    System.out.println("Se presionó el click izquierdo");
                }

                if (boton.getIcon() != imgBandera) {//si se oprime click izq se verifica que no haya una bandera en la imagen
                    System.out.println("El botón no tiene bandera");
                    if ((ImageIcon) minas[x][y].getIcon() == imgMina) {//si bajo el boton hay un label con imagen  "imgMina"
                        minas[x][y].setIcon(imgMinaExplotada);//coloca en su lugar la imagen de la mina con fondo rojo
                        minas[x][y].repaint();//recarga la imagen
                        juegoTerminado = true;
                        tiempo.setSegundos(0);
                        tiempo.detener();
                        for (int i = 0; i < minas.length; i++) {//al haber descubierto una mina se recorre la matriz de minas
                            for (int j = 0; j < minas.length; j++) {
                                if (minas[i][j].getIcon() == imgMina) {//en las posiciones del array que se encuentre la imagen de una mina
                                    System.out.println("Se abre el botón (" + x + ", " + y + ")");
                                    botones[i][j].setVisible(false);//se pone invisible el boton para mostrar la imagen de la mina
                                }
                            }
                        }
                    }

                    boton.setVisible(false);//en caso que no haya una mina bajo el boton, simplemente se deja ver lo que hay debajo
                    System.out.println("Se abre el botón (" + x + ", " + y + ")");
                    abrirMinasVacias(minasAlrededor(x, y));
                    System.out.println("Se llama al método abrirMInasVacias()");
                    ganarJuego();

                }
            }
        }
    }

    private void botonMinaMousePressed(MouseEvent evt) {
        if (!juegoTerminado) {
            if (evt.getModifiers() == 16) {
                botonCarita.setIcon(caritaAsombradaBoton);
            }
        }
    }

    private void botonMinaMouseReleased(MouseEvent evt) {
        JButton boton = (JButton) evt.getSource();//se crea un objeto JBuuton y se inicializa con el objeto fuente del evento
        x = (boton.getX() - 1) / 29;//se inicializan las variables "x" e "y" con los valores de la posicion respectiva a cada eje del
        y = (boton.getY() - 2) / 29;//objeto obtenido del evento y se aplica la fórmula inversa que devolverá la posición del objeto en la matriz
        if (!juegoTerminado) {
            if (evt.getModifiers() == 16) {
                if (botones[x][y].getIcon() != imgBandera) {
                    if (minas[x][y].getIcon() != imgMina) {
                        botonCarita.setIcon(caritaFelizBoton);

                    } else {
                        botonCarita.setIcon(caritaTristeBoton);
                    }

                } else {
                    botonCarita.setIcon(caritaFelizBoton);
                }
            }
        }
    }

    private void ganarJuego() {
        int contador = 0;
        for (int i = 0; i < minas.length; i++) {
            for (int j = 0; j < minas.length; j++) {
                if (minas[i][j].getIcon() == null && !botones[i][j].isVisible()) {
                    System.out.println("Mina abierta");
                    contador++;
                }
            }
        }
        if (contador == (minas.length * minas.length - cantidadMinas)) {
            juegoTerminado = true;
            tiempo.setSegundos(0);
            tiempo.detener();
            System.out.println("Se detuvo el tiempo");
            JOptionPane.showMessageDialog(null, "¡FELICITACIONES, \n    GANASTE!\n TIEMPO: " + labelSegundos.getText() + " segundos");
        } else {
            System.out.println("No has ganado");

        }
    }

    private void resetearJuego() {
        for (int i = 0; i < minas.length; i++) {
            for (int j = 0; j < minas.length; j++) {
                botones[i][j].setVisible(true);
                botones[i][j].setIcon(tapaMina);
                minas[i][j].setText("0");
                if (minas[i][j].getIcon() != null) {
                    if ((minas[i][j].getIcon().equals(imgMina)) || (minas[i][j].getIcon().equals(imgMinaExplotada))) {
                        minas[i][j].setIcon(null);
                    }
                }
            }
        }
        creadorDeMinas();
        contadorInicio = 0;
        labelBanderas.setText(Integer.toString(cantidadMinas));
        botonCarita.setIcon(caritaFelizBoton);
        System.out.println("Se reinició el juego");
        juegoTerminado = false;
        labelSegundos.setText(String.valueOf(0));
    }

    private List<JLabel> minasAlrededor(int x, int y) {
        List<JLabel> posiciones = new ArrayList<>();
        int posicionX = x;
        int posicionY = y;
        for (int i = 0; i < 8; i++) {
            switch (i) {                    //Se indica el la posicion de la mina respecto al click en cada case
                case 0:
                    posicionY = posicionY - 1;        // 0 X 0
                                                      // 0 M 0 
                    break;                            // 0 0 0
                case 1:
                    posicionX = posicionX - 1;        // X 0 0
                                                      // 0 M 0
                    break;                            // 0 0 0
                case 2:
                    posicionY = posicionY + 1;        // 0 0 0
                                                      // X M 0
                    break;                            // 0 0 0
                case 3:
                    posicionY = posicionY + 1;        // 0 0 0
                                                      // 0 M 0
                    break;                            // X 0 0
                case 4:
                    posicionX = posicionX + 1;        // 0 0 0
                                                      // 0 M 0
                    break;                            // 0 X 0
                case 5:
                    posicionX = posicionX + 1;        // 0 0 0
                                                      // 0 M 0
                    break;                            // 0 0 X
                case 6:
                    posicionY = posicionY - 1;        // 0 0 0
                                                      // 0 M X
                    break;                            // 0 0 0
                case 7:
                    posicionY = posicionY - 1;        // 0 0 X
                                                      // 0 M 0
                    break;                            // 0 0 0
            }
            if (posicionX >= 0 && posicionX < minas.length && posicionY >= 0 && posicionY < minas.length) {
                posiciones.add(minas[posicionX][posicionY]);
            }
        }
        return posiciones;
    }

    private void abrirMinasVacias(List posiciones) {
        for (int i = 0; i < posiciones.size(); i++) {
            System.out.println("tamaño list: " + posiciones.size());
            JLabel minaObtenida = (JLabel) posiciones.get(i);
            int posX = (minaObtenida.getX() - 1) / 29;
            int posY = (minaObtenida.getY() - 2) / 29;
            if (botones[posX][posY].isVisible()) {
                if (minaObtenida.getIcon() != imgMina && botones[posX][posY].getIcon() != imgBandera) {
                    System.out.println("El label no tiene mina");
                    System.out.println("x:" + posX + ", y:" + posY);
                    botones[posX][posY].setVisible(false);
                    if (minaObtenida.getText().equals("0")) {
                        abrirMinasVacias(minasAlrededor(posX, posY));
                        System.out.println("pasamos posicion\n x:" + posX + ", y:" + posY);
                    }
                }
            }
        }
    }


    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelSegundos = new javax.swing.JLabel();
        labelBanderas = new javax.swing.JLabel();
        botonCarita = new javax.swing.JButton();
        fondo = new javax.swing.JLabel();
        marco = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setMinimumSize(new java.awt.Dimension(669, 692));
        setPreferredSize(new java.awt.Dimension(669, 669));
        setResizable(false);
        getContentPane().setLayout(null);

        labelSegundos.setBackground(new java.awt.Color(0, 0, 0));
        labelSegundos.setFont(new java.awt.Font("Leelawadee", 0, 48)); // NOI18N
        labelSegundos.setForeground(new java.awt.Color(255, 0, 0));
        labelSegundos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelSegundos.setText("0");
        labelSegundos.setOpaque(true);
        getContentPane().add(labelSegundos);
        labelSegundos.setBounds(553, 18, 100, 60);

        labelBanderas.setBackground(new java.awt.Color(0, 0, 0));
        labelBanderas.setFont(new java.awt.Font("Leelawadee", 0, 48)); // NOI18N
        labelBanderas.setForeground(new java.awt.Color(255, 0, 0));
        labelBanderas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelBanderas.setText("40");
        labelBanderas.setOpaque(true);
        getContentPane().add(labelBanderas);
        labelBanderas.setBounds(16, 18, 100, 60);

        botonCarita.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/botonCarita.png"))); // NOI18N
        botonCarita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCaritaActionPerformed(evt);
            }
        });
        getContentPane().add(botonCarita);
        botonCarita.setBounds(305, 18, 60, 60);

        fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/fondoBuscaminas.png"))); // NOI18N
        getContentPane().add(fondo);
        fondo.setBounds(15, 93, 640, 640);

        marco.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/marcoBuscaminas.png"))); // NOI18N
        getContentPane().add(marco);
        marco.setBounds(0, 0, 680, 750);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonCaritaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCaritaActionPerformed
        resetearJuego();

    }//GEN-LAST:event_botonCaritaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JuegoIntermedio.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JuegoIntermedio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCarita;
    private javax.swing.JLabel fondo;
    private javax.swing.JLabel labelBanderas;
    private javax.swing.JLabel labelSegundos;
    private javax.swing.JLabel marco;
    // End of variables declaration//GEN-END:variables

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

}
