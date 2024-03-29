package courseOrganizer.models;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JCheckBox;

import courseOrganizer.listeners.ButtonMouseListener;
import courseOrganizer.views.MainWindow;
import courseOrganizer.views.addCourseViews.AddCourseOptionWindow;
import courseOrganizer.views.assignmentWindows.AddAssignmentWindow;
import courseOrganizer.views.deleteViews.DeleteCourseWindow;
import courseOrganizer.models.ScrollPane;

public class LeftToolBar extends JPanel
{
	private JButton addButton;
	private JButton deleteButton;
	private JButton searchButton;
	private JButton courseFinished;
	private JPanel panel; 
        private Font font;
        
        private JCheckBox boldCheckBox;
        private JCheckBox italicCheckBox;
	
	private MainWindow container; // Μεταβλητή JFrame με extended λειτουργίες.
	private CourseList cl;
        
        private String mode;
        
	public LeftToolBar(MainWindow container, CourseList cl)
	{
		panel = this;
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		this.container = container;
		this.cl = cl;
		
		ImageIcon plusSign = new ImageIcon("add.png");
		ImageIcon minusSign = new ImageIcon("remove.png");
		ImageIcon searchGlass = new ImageIcon("magnifying glass.png");
		ImageIcon checkMark = new ImageIcon("ok.png");
		/*ImageIcon notepad = new ImageIcon("stock_notes.png");
		ImageIcon pencil = new ImageIcon("edit.png");*/
		
		addButton = new JButton(plusSign);
		addButton.setToolTipText("Add a course or assignment to the list");
		addButton.addMouseListener(new ButtonMouseListener(addButton));
		//addButton.setBackground(ACCENT_BLUE);
		
		deleteButton = new JButton(minusSign);
		deleteButton.setToolTipText("Delete a course or assignment from the list");
		deleteButton.addMouseListener(new ButtonMouseListener(deleteButton));
		
		//System.out.println("DeleteButton maximum width = " + deleteButton.getMaximumSize().width + " and height = " + deleteButton.getMaximumSize().height);
		
		searchButton = new JButton(searchGlass);
		searchButton.setMaximumSize(deleteButton.getMaximumSize());
		searchButton.setToolTipText("Search the list for a term");
		searchButton.addMouseListener(new ButtonMouseListener(searchButton));
		
		panel.setMaximumSize(new Dimension(deleteButton.getMaximumSize().width, 800));
				
		panel.add(addButton);
		panel.add(deleteButton);
		panel.add(searchButton);
		/*panel.add(courseFinished);
		panel.add(showAssignments);
		panel.add(showCourses);
		panel.add(notes);
		panel.add(edit);*/
		
		panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
	}
	
        // Μέθοδος που καλείται από τη MainWindow και περνάει το mode που έχει επιλέξει ο χρήστης.
	public void setViewMode(String viewMode)
	{
		if (viewMode.equals("Courses"))
		{
			changeToCourseMode();
		}
		else if (viewMode.equals("Assignments"))
		{
			changeToAssignmentMode();
		}
		else if (viewMode.equals("Notepaper"))
		{
			changeToNotepaperMode();
		}
		else if (viewMode.equals("Notebook"))
		{
			
		}
                
                mode = viewMode; // Η μεταβλητή mode χρησιμοποιείται στη μέθοδο resizeComponent()
	}
	
        // Μέθοδος που ρυθμίζει τι θα εμφανίζει το leftToolBar όταν το mode είναι Notepaper
	private void changeToNotepaperMode()
        {
            // Όταν το Mode είναι Notepaper δε θα εμφανίζονται τα υπόλοιπα κουμπιά
            addButton.setVisible(false);
            deleteButton.setVisible(false);
            searchButton.setVisible(false);

            boldCheckBox = new JCheckBox ("Bold");
            italicCheckBox = new JCheckBox ("Italic");
            
            CheckBoxHandler handler = new CheckBoxHandler();
            boldCheckBox.addItemListener(handler);
            italicCheckBox.addItemListener(handler);
        }
            
        /* Εσωτερική κλάση για το χειρισμό συμβάντων ItemListener.
         * Τέτοια συμβάντα προκαλούνται από αντικείμενα τύπου checkbox.*/
        private class CheckBoxHandler implements ItemListener
        {
                @Override
                public void itemStateChanged (ItemEvent event)
                {
                                        
                        if (boldCheckBox.isSelected() && italicCheckBox.isSelected())                     
                            font = new Font ("Serif", Font.BOLD + Font.ITALIC, 35);
                        else if (boldCheckBox.isSelected())
                            font = new Font ("Serif", Font.BOLD, 35);
                        else if (italicCheckBox.isSelected())
                            font = new Font ("Serif", Font.ITALIC, 35);
                        else
                            font = new Font ("Serif", Font.PLAIN, 35);
                                            
                }
        }
            	
	private void changeToAssignmentMode()
	{
		removeActionListener(addButton);
		addButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new AddAssignmentWindow(container, cl);
			}
		});
		removeActionListener(deleteButton);
	}

	private void changeToCourseMode()
	{
		removeActionListener(addButton);
		addButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
                                // Όταν πατηθεί το "+" ανοίγει το παράθυρο για προσθήκη μαθήματος.
				new AddCourseOptionWindow(container, cl);
			}
		});
		removeActionListener(deleteButton);
		deleteButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (cl.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "No courses found.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
					new DeleteCourseWindow(container, cl);
			}
		});
	}
	
        // Μέθοδος η οποία "πετάει" τον ακροατή συμβάντων για τα κουμπιά add και delete.
	private void removeActionListener(JButton button)
	{
		if (button.getActionListeners().length > 0)
		{
			button.removeActionListener(button.getActionListeners()[0]);
		}
	}

        // Καλείται από τη MainWindow
	public void resizeComponents()
	{
            if (mode == "Notepaper"){
                panel.add(boldCheckBox);
                panel.add(italicCheckBox);
            }else{
		panel.removeAll();
		addButton.setMaximumSize(new Dimension(this.getMaximumSize().width, (this.getMaximumSize().height * 6 )/ 100));
		deleteButton.setMaximumSize(new Dimension(this.getMaximumSize().width, (this.getMaximumSize().height * 6 )/ 100));
		searchButton.setMaximumSize(new Dimension(this.getMaximumSize().width, (this.getMaximumSize().height * 6 )/ 100));
		panel.add(addButton);
		panel.add(deleteButton);
		panel.add(searchButton);
		panel.validate(); // Ανανεώνει το περιεχόμενο του panel με τα νέα κουμπιά και τη νεά λειτουργικότητα
            }
	}
	
}
