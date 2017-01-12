import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.io.File;

class GUI {
	public static void main(String[] args){
		Board board = new Board();
	}
}



class Board extends JComponent {
    
	JButton losninger, neste, forrige, clear, loadNew;
	JFrame drawFrame;
	JPanel sudokuBrett;
	JPanel btnPan;
	MyListener ml;
	SudokuBeholder beholder = new SudokuBeholder();
    
	public Board(){
		//Init panel, frame, content
		drawFrame = new JFrame("Sudoku");
		drawFrame.setLayout(new BorderLayout());
		
		sudokuBrett = new JPanel();
		sudokuBrett.setPreferredSize(new Dimension(300, 300));
		sudokuBrett.setLayout(new BorderLayout());
		drawFrame.add(sudokuBrett, BorderLayout.NORTH);
		
		btnPan = new JPanel();
		this.setEnabled(true);
		this.setPreferredSize(new Dimension(400, 400));
        
		//Setter storrelser
		btnPan.setPreferredSize(new Dimension(30, 60));
        
		//Ordne knappene
		losninger = new JButton("Vis løsninger");
		neste = new JButton("Neste");
		forrige = new JButton("Forrige");
        loadNew = new JButton("Velg ny fil");
        
        
		//Legger inn knapper
		btnPan.add(losninger);
		btnPan.add(forrige);
        btnPan.add(neste);
		btnPan.add(loadNew);
        
		//Legger paa action listeners
		Action act = new Action();
		losninger.addActionListener(act);
		neste.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Brett brett = beholder.taUt();
				if(brett != null)
					tegnBrett(brett);
			}
		});
		forrige.addActionListener(act);
		loadNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				apneFil();
			}
		});
		apneFil();
        
		//Fullforer vinduet
		drawFrame.setSize(800,600);
		this.setBounds(0,0,400,400);
		drawFrame.add(btnPan, BorderLayout.SOUTH);
        
		
        ml = new MyListener();
		this.addMouseListener(ml);
		this.addMouseMotionListener(ml);
        
		this.setVisible(true);
		drawFrame.setVisible(true);
        
		drawFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
    
	private void apneFil() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(drawFrame);
        File file = null;
        do {
            file = chooser.getSelectedFile();
        } while(file == null);
        
        Brett brett = new Brett(file.getAbsolutePath());
		
		brett.lesInn();
		brett.los(beholder);
		Brett losning = beholder.taUt();
		tegnBrett(losning);
		losning.skrivUt();
	}
	
	private void tegnBrett(Brett brett) {
		sudokuBrett.removeAll();
		sudokuBrett.setVisible(false);
		sudokuBrett.setLayout(new GridLayout(0,6));
		for(int j = 0; j < brett.hentDimensjon(); j++) {
			JPanel rad = new JPanel();
			rad.setLayout(new GridLayout(6, 0));
			for(int i = 0; i < brett.hentDimensjon(); i++) {
				Rute rute = brett.hentRuter()[i][j];
				JLabel ruteText = new JLabel(rute.verdi.toString());
				ruteText.setSize(30, 30);
				rad.add(ruteText);
			}
			sudokuBrett.add(rad);
		}
		sudokuBrett.setVisible(true);
	}
    
	class Action implements ActionListener{
		public void actionPerformed(ActionEvent e){
			
		}
	}
    
	class MyListener extends MouseAdapter {
        
		public void mousePressed(MouseEvent e){
			
		}
    }
}




