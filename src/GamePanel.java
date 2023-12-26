import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements ActionListener {
    final int SCREEN_WIDTH = 800;
    final int SCREEN_HEIGHT = 500;
    final int UNIT_SIZE = 20;
    final int DELAY = 75;
    final int DISC_SIZE = 5;
    final int X[] = new int[DISC_SIZE];
    char direction = 'N';
    int ballX = SCREEN_WIDTH/2;
    int ballY = SCREEN_HEIGHT/2;
    char ballDirectionX = 'L';
    char ballDirectionY = 'U';
    int score = 0;
    boolean running = false;
    Timer timer;

    GamePanel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        for (int i = 0; i < DISC_SIZE; i++) {
            X[i] = i * UNIT_SIZE; // Adjust the spacing as needed
        }
        moveBall();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void moveBall(){
        moveBallX();
        moveBallY();
    }
    public void moveBallX(){
        switch(ballDirectionX){
            case 'L':
                ballX -= UNIT_SIZE;
                break;
            case 'R':
                ballX += UNIT_SIZE;
                break;
        }
    }
    public void moveBallY(){
        switch(ballDirectionY){
            case 'U':
                ballY -= UNIT_SIZE;
                break;
            case 'D':
                ballY += UNIT_SIZE;
                break;
        }
    }
    public void reverseBallY(){
        if(ballDirectionY == 'U'){
            ballDirectionY = 'D';
        }
        else if(ballDirectionY == 'D'){
            ballDirectionY = 'U';
        }
    }
    public void reverseBallX(){
        if(ballDirectionX == 'L'){
            ballDirectionX = 'R';
        }
        else if(ballDirectionX == 'R'){
            ballDirectionX = 'L';
        }
    }
    public void moveDisc(){
        switch(direction) {
            case 'R':
                for (int i = 0; i < DISC_SIZE; i++) {
                    X[i] += UNIT_SIZE;
                }
                break;
            case 'L':
                for (int i = 0; i < DISC_SIZE; i++) {
                    X[i] -= UNIT_SIZE;
                }
                break;
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(running) {
            //drawing ball
            g.setColor(Color.yellow);
            g.fillOval(ballX, ballY, UNIT_SIZE, UNIT_SIZE);

            //drawing disc
            for (int i = 0; i < DISC_SIZE; i++) {
                g.setColor(Color.white);
                g.fillRect(X[i] + UNIT_SIZE, SCREEN_HEIGHT - UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }

            //score
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("Score: "+score, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+score))/2, g.getFont().getSize());
            //credits
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Game by GAUTAM ARORA: "+score, (SCREEN_WIDTH - metrics2.stringWidth("Game by GAUTAM ARORA"))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }

    public void checkBallCollision(){
        if ((ballX >= X[0] - UNIT_SIZE) && (ballX <= X[DISC_SIZE-1] + UNIT_SIZE) && ballY >= SCREEN_HEIGHT- 2 * UNIT_SIZE) {
            reverseBallY();
            score++;
        }
        if(ballY<UNIT_SIZE){
            reverseBallY();
        }
        if(ballY>=SCREEN_HEIGHT){
            running = false;
            timer.stop();
        }
        if(ballX>=SCREEN_WIDTH || ballX<UNIT_SIZE){
            reverseBallX();
        }
    }
    public void gameOver(Graphics g){
        //Game Over text
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);

        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+score, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+score))/2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            moveBall();
            if(direction !='N'){
                moveDisc();
            }
            checkBallCollision();
        }
        repaint();
    }

    private class MyKeyAdapter implements KeyListener {
        
        @Override
        public void keyTyped(KeyEvent e) {  }

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                        direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                        direction = 'R';
                    break;
            }
    }

        @Override
        public void keyReleased(KeyEvent e) {
            direction = 'N';
        }
    }
}