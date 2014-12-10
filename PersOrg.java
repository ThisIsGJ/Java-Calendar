import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.sql.Date;
import javax.swing.border.*;
import java.awt.BorderLayout;


public class PersOrg extends JFrame {

	private JMenu mFile;
	private JMenuItem cCont;
	private JMenuItem cCal;
	private JMenuItem mIExit;
	private JPanel mainPanel;
	private JPanel msg = new JPanel();
	private JTextArea msgArea;
	private JButton arrowRight;
	private JTextField date;
	private JTextField time;
	private JTextField location;
	private JTextField subject;
	private JTextArea description;
	
	private JTextField fNameField;
	private JTextField lNameField;
	private JTextField phoneField;
	private JTextField mobileField;
	private JTextField companyField;
	private JTextField pictureField;
	
	private Quotes quotes = new Quotes();
	private Date curDate = new Date(System.currentTimeMillis());
	private Date lastVisited;
	
	private final Border blackline = BorderFactory.createLineBorder(Color.black); // Used on GUI elements
	private final Border empty = BorderFactory.createEmptyBorder(3,5,5,5);		  // Used on GUI elements
	
	private final Icon ARROWLEFT = createImageIcon("images/arrowleft.png");
	private final Icon ARROWRIGHT = createImageIcon("images/arrowright.png");
	private final Icon CLOSE = createImageIcon("images/x.png");
	private final Icon EDIT = createImageIcon("images/e.png");
	private final Icon MINUS = createImageIcon("images/minus.png");
	private final Icon PLUS = createImageIcon("images/plus.png");
	private final Icon CLEAR = createImageIcon("images/c.png");
	
	private final int MAIN_SCREEN = 0;
	private final int CALENDAR_SCREEN = 1;
	private final int CALENDAR_EDIT = 2;
	private final int CONTACT_SCREEN = 3;
	private final int CONTACT_EDIT = 4;
	private final int CALENDAR_ADD = 5;
	private final int CONTACT_ADD = 6;
	
	private int curDay = 01;
	private String day;
	private int curMonth = 01;
	private String month;
	private long curTime;
	
	private ArrayList<CalItem> allAppoint;
	private ArrayList<Contact> allCont;
	private ArrayList<JButton> allEdit;
	private CalItem curAppoint;
	private Contact curCont;
	long curDateEdit;
	
	private final int MAX_QUOTES = 3;
	private int quoteNum = 1;
	
	Calendar myCal = new Calendar("data/cal.txt");
	ContactBook myCont = new ContactBook("data/cont.txt");
	
	private final ArrayList<String> months = new ArrayList<String>();

	public PersOrg() {
		months.add("January"); months.add("February"); months.add("March"); months.add("April"); months.add("May"); months.add("June"); 
		months.add("July"); months.add("August"); months.add("September"); months.add("October"); months.add("November"); months.add("December"); 
		initGUI();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Personal Organiser");
		setResizable(false);
		setLayout(null);
		// If it's a new day, display the message of the day box again
		if (lastVisited != curDate) {
			changeQuote();
			showMsgBox();
		}
	}
	
	private void initGUI() {
	
		JMenuBar mBar = new JMenuBar();
        mFile = new JMenu("File");
		
		cCont = new JMenuItem("Clear Contacts");
        cCont.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clearContAction(evt);
            }
        });
		
		cCal = new JMenuItem("Clear Calendar");
        cCal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clearCalAction(evt);
            }
        });
		
		mIExit = new JMenuItem("Exit");
        mIExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                exitAction(evt);
            }
        });

		
		mBar.add(mFile);
        mFile.add(cCont);
        mFile.add(cCal);
		mFile.add(new JSeparator());
        mFile.add(mIExit);
		
		setJMenuBar(mBar);

		JLayeredPane myPane = new JLayeredPane();
		myPane.setBounds(0, 0, 188, 264);
		add(myPane);
		
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 188, 264);
		mainPanel.setLayout(null);
		mainPanel.setBackground(Color.white);
		myPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
		
		msg.setBounds(2,2,184,82);
		msg.setLayout(null);
		msg.setBackground(Color.gray);
		msg.setBorder(blackline);
		msg.setVisible(false);
		myPane.add(msg, JLayeredPane.PALETTE_LAYER);
		
		JButton close = new JButton(CLOSE);
		close.setBounds(170, 1, 13, 14);
		close.setBorder(empty);
		close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                hideMsgBox();
            }
        });
		msg.add(close);
		
		arrowRight = new JButton(ARROWRIGHT);
		arrowRight.setBounds(167, 57, 16, 16);
		arrowRight.setBorder(empty);
		arrowRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				quoteNum++;
                changeQuote();
            }
        });
		msg.add(arrowRight);
		
		msgArea = new JTextArea();
		msgArea.setBounds(4, 4, 166, 76);
		msgArea.setEnabled(false);
		msgArea.setBackground(Color.gray);
		msgArea.setLineWrap(true);
		msgArea.setWrapStyleWord(true);
		msgArea.setDisabledTextColor(Color.black);
		msg.add(msgArea);
		
		populateMain();
	}
	
	private void populateMain() {
		curTime = System.currentTimeMillis();
		Date curDate = new Date(curTime);
		day = curDate.toString().substring(8, 10);
		curDay = Integer.parseInt(day);
		curMonth = Integer.parseInt(curDate.toString().substring(5, 7));
		month = months.get(curMonth);
		
		JButton calendarButton = new JButton("Calendar");
		calendarButton.setBounds(20, 25, 148, 35);
		calendarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				transitionMainPanel(CALENDAR_SCREEN);
            }
        });
		mainPanel.add(calendarButton);
		
		JButton contactButton = new JButton("Contacts");
		contactButton.setBounds(20, 75, 148, 35);
		contactButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				transitionMainPanel(CONTACT_SCREEN);
            }
        });
		mainPanel.add(contactButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.setBounds(20, 190, 148, 35);
		exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				System.exit(1);
            }
        });
		mainPanel.add(exitButton);
	}
	
	private void populateCalendar() {
		
		JTextField titleLabel = new JTextField(day + " " + month);
		titleLabel.setBounds(18, 8, 152, 30);
		titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		titleLabel.setEnabled(false);
		titleLabel.setBorder(empty);
		titleLabel.setOpaque(true);
		titleLabel.setDisabledTextColor(Color.black);
		titleLabel.setHorizontalAlignment(JTextField.CENTER);
		mainPanel.add(titleLabel);
		
		JButton leftArrow = new JButton(ARROWLEFT);
		leftArrow.setBounds(1, 16, 16, 16);
		leftArrow.setBorder(empty);
		leftArrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				changeDates(-86400000L);
				transitionMainPanel(CALENDAR_SCREEN);
            }
        });
		mainPanel.add(leftArrow);
		
		JButton rightArrow = new JButton(ARROWRIGHT);
		rightArrow.setBounds(171, 16, 16, 16);
		rightArrow.setBorder(empty);
		rightArrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				changeDates(86400000L);
				transitionMainPanel(CALENDAR_SCREEN);
            }
        });
		mainPanel.add(rightArrow);
		
		JButton closeCal = new JButton(CLOSE);
		closeCal.setBounds(172, 1, 13, 14);
		closeCal.setBorder(empty);
		closeCal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                transitionMainPanel(MAIN_SCREEN);
            }
        });
		mainPanel.add(closeCal);
		
		JButton addButton = new JButton(PLUS);
		addButton.setBounds(70, 37, 16, 16);
		addButton.setBorder(empty);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				transitionMainPanel(CALENDAR_ADD);
			}
		});
		mainPanel.add(addButton);
		
		allAppoint = myCal.getAppointPerDate(curTime);
		
		JButton clearButton = new JButton(CLEAR);
		clearButton.setBounds(95, 37, 16, 16);
		clearButton.setBorder(empty);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				for (int i = 0; i < allAppoint.size(); i++) {
					myCal.deleteItem(allAppoint.get(i));
				}
				transitionMainPanel(CALENDAR_SCREEN);
			}
		});
		mainPanel.add(clearButton);
		
		JScrollPane myScrollPane = new JScrollPane();
		myScrollPane.setBounds(2, 56, 184, 184);
		myScrollPane.setBorder(blackline);
		myScrollPane.setLayout(null);
		myScrollPane.setBackground(Color.gray);
		mainPanel.add(myScrollPane);
		
		allEdit = new ArrayList<JButton>();
		for (int i = 0; i < Math.min(allAppoint.size(), 7); i++) {
			JTextField curField = new JTextField(allAppoint.get(i).getSubj());
			curField.setBounds(2, (25 * i) + 4, 160, 21);
			curField.setFont(new Font("Arial", Font.PLAIN, 10));
			curField.setEnabled(false);
			curField.setBorder(empty);
			curField.setOpaque(true);
			curField.setBackground(Color.gray);
			curField.setDisabledTextColor(Color.black);
			myScrollPane.add(curField);
			
			JButton editButton = new JButton(EDIT);
			editButton.setBounds(160, (25 * i) + 4, 16, 16);
			editButton.setBorder(empty);
			editButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					for (int e = 0; e < allEdit.size(); e++) {
						if (allEdit.get(e) == evt.getSource()) {
							curAppoint = allAppoint.get(e);
						}
					}
					transitionMainPanel(CALENDAR_EDIT);
				}
			});
			allEdit.add(editButton);
			myScrollPane.add(editButton);
		}
		if (allAppoint.size() <= 0) {
			JTextField curField = new JTextField("No Appointments");
			curField.setBounds(2, 4, 160, 21);
			curField.setFont(new Font("Arial", Font.PLAIN, 10));
			curField.setEnabled(false);
			curField.setBorder(empty);
			curField.setOpaque(true);
			curField.setBackground(Color.gray);
			curField.setDisabledTextColor(Color.black);
			myScrollPane.add(curField);
			return;
		}
	}
	
	private void populateCalEdit(final String type) {
		// curAppoint is the appointment that we edited. It is the actual object
		JTextField titleLabel = new JTextField("Appointment");
		titleLabel.setBounds(18, 8, 130, 30);
		titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		titleLabel.setEnabled(false);
		titleLabel.setBorder(empty);
		titleLabel.setOpaque(true);
		titleLabel.setDisabledTextColor(Color.black);
		titleLabel.setHorizontalAlignment(JTextField.CENTER);
		mainPanel.add(titleLabel);
		
		JButton leftArrow = new JButton(ARROWLEFT);
		leftArrow.setBounds(1, 16, 16, 16);
		leftArrow.setBorder(empty);
		leftArrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				transitionMainPanel(CALENDAR_SCREEN);
            }
        });
		mainPanel.add(leftArrow);
		
		if (type == "filled") {
			JButton minusButton = new JButton(MINUS);
			minusButton.setBounds(146, 16, 16, 16);
			minusButton.setBorder(empty);
			minusButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					myCal.deleteItem(curAppoint);
					transitionMainPanel(CALENDAR_SCREEN);
				}
			});
			mainPanel.add(minusButton);
		}
		
		JButton editButton = new JButton(type == "blank" ? PLUS : EDIT);
		editButton.setBounds(166, 16, 16, 16);
		editButton.setBorder(empty);
		editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				// Commit the edit here
				if (type == "blank") {
					commitAdd();
				} else {
					commitEdit();
				}
				transitionMainPanel(CALENDAR_SCREEN);
            }
        });
		mainPanel.add(editButton);
		
		curDateEdit = (type == "blank" ? curTime : curAppoint.getDateTime());
		String tempDate = new Date(curDateEdit).toString();
		String tempMonth = months.get(Integer.parseInt(tempDate.substring(5, 7)));
		String theDate = tempDate.substring(8, 10) + " " + tempMonth;
		
		String theTime = "Time";
		String theLocation = "Location";
		String theSubject = "Subject";
		String theDesc = "Description";
		
		if (type == "filled") {
			theTime = curAppoint.getTime();
			theLocation = curAppoint.getLocation();
			theSubject = curAppoint.getSubj();
			theDesc = curAppoint.getDesc();
		}
		
		date = new JTextField(theDate);
		date.setBounds(32, 50, 124, 24);
		date.setFont(new Font("Arial", Font.PLAIN, 12));
		date.setEnabled(false);
		date.setDisabledTextColor(Color.black);
		mainPanel.add(date);
		
		JButton dateLeftArrow = new JButton(ARROWLEFT);
		dateLeftArrow.setBounds(5, 52, 16, 16);
		dateLeftArrow.setBorder(empty);
		dateLeftArrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				curDateEdit -= 86400000L;
				String tempDate = new Date(curDateEdit).toString();
				String tempMonth = months.get(Integer.parseInt(tempDate.substring(5, 7)));
				date.setText(tempDate.substring(8, 10) + " " + tempMonth);
            }
        });
		mainPanel.add(dateLeftArrow);
		
		JButton dateRightArrow = new JButton(ARROWRIGHT);
		dateRightArrow.setBounds(166, 52, 16, 16);
		dateRightArrow.setBorder(empty);
		dateRightArrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				curDateEdit += 86400000L;
				String tempDate = new Date(curDateEdit).toString();
				String tempMonth = months.get(Integer.parseInt(tempDate.substring(5, 7)));
				date.setText(tempDate.substring(8, 10) + " " + tempMonth);
            }
        });
		mainPanel.add(dateRightArrow);
		
		time = new JTextField(theTime);
		time.setBounds(5, 78, 178, 24);
		time.setFont(new Font("Arial", Font.PLAIN, 12));
		mainPanel.add(time);
		
		location = new JTextField(theLocation);
		location.setBounds(5, 106, 178, 24);
		location.setFont(new Font("Arial", Font.PLAIN, 12));
		mainPanel.add(location);
		
		subject = new JTextField(theSubject);
		subject.setBounds(5, 134, 178, 24);
		subject.setFont(new Font("Arial", Font.PLAIN, 12));
		mainPanel.add(subject);
		
		description = new JTextArea(theDesc);
		description.setBounds(5, 162, 178, 70);
		description.setFont(new Font("Arial", Font.PLAIN, 12));
		description.setBorder(blackline);
		mainPanel.add(description);
	}
	
	private void commitEdit() {
		curAppoint.setDateTime(curDateEdit);
		curAppoint.setTime(time.getText());
		curAppoint.setLocation(location.getText());
		curAppoint.setSubj(subject.getText());
		curAppoint.setDesc(description.getText());
	}
	
	private void commitAdd() {
		myCal.addItem(curDateEdit, time.getText(), location.getText(), subject.getText(), description.getText());
	}
	
	private void populateContact() {		
		JTextField titleLabel = new JTextField("Contacts");
		titleLabel.setBounds(18, 8, 152, 30);
		titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		titleLabel.setEnabled(false);
		titleLabel.setBorder(empty);
		titleLabel.setOpaque(true);
		titleLabel.setDisabledTextColor(Color.black);
		titleLabel.setHorizontalAlignment(JTextField.CENTER);
		mainPanel.add(titleLabel);
		
		JButton closeCal = new JButton(CLOSE);
		closeCal.setBounds(172, 1, 13, 14);
		closeCal.setBorder(empty);
		closeCal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                transitionMainPanel(MAIN_SCREEN);
            }
        });
		mainPanel.add(closeCal);
		
		JButton addButton = new JButton(PLUS);
		addButton.setBounds(70, 37, 16, 16);
		addButton.setBorder(empty);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				transitionMainPanel(CONTACT_ADD);
			}
		});
		mainPanel.add(addButton);
		
		allCont = myCont.getContacts();
		
		JButton clearButton = new JButton(CLEAR);
		clearButton.setBounds(95, 37, 16, 16);
		clearButton.setBorder(empty);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				for (int i = 0; i < allCont.size(); i++) {
					myCont.deleteItem(allCont.get(i));
				}
				transitionMainPanel(CONTACT_SCREEN);
			}
		});
		mainPanel.add(clearButton);
		
		JScrollPane myScrollPane = new JScrollPane();
		myScrollPane.setBounds(2, 56, 184, 184);
		myScrollPane.setBorder(blackline);
		myScrollPane.setLayout(null);
		myScrollPane.setBackground(Color.gray);
		mainPanel.add(myScrollPane);
		
		allEdit = new ArrayList<JButton>();
		for (int i = 0; i < allCont.size(); i++) {
			JTextField curField = new JTextField(allCont.get(i).getFullName());
			curField.setBounds(2, (25 * i) + 4, 160, 21);
			curField.setFont(new Font("Arial", Font.PLAIN, 10));
			curField.setEnabled(false);
			curField.setBorder(empty);
			curField.setOpaque(true);
			curField.setBackground(Color.gray);
			curField.setDisabledTextColor(Color.black);
			myScrollPane.add(curField);
			
			JButton editButton = new JButton(EDIT);
			editButton.setBounds(160, (25 * i) + 4, 16, 16);
			editButton.setBorder(empty);
			editButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					for (int e = 0; e < allEdit.size(); e++) {
						if (allEdit.get(e) == evt.getSource()) {
							curCont = allCont.get(e);
						}
					}
					transitionMainPanel(CONTACT_EDIT);
				}
			});
			allEdit.add(editButton);
			myScrollPane.add(editButton);
		}
		if (allCont.size() <= 0) {
			JTextField curField = new JTextField("No Contacts");
			curField.setBounds(2, 4, 160, 21);
			curField.setFont(new Font("Arial", Font.PLAIN, 10));
			curField.setEnabled(false);
			curField.setBorder(empty);
			curField.setOpaque(true);
			curField.setBackground(Color.gray);
			curField.setDisabledTextColor(Color.black);
			myScrollPane.add(curField);
			return;
		}
	}
	
	private void populateContEdit(final String type) {
		// curCont is the appointment that we edited. It is the actual object
		JTextField titleLabel = new JTextField("Contact");
		titleLabel.setBounds(18, 8, 130, 30);
		titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		titleLabel.setEnabled(false);
		titleLabel.setBorder(empty);
		titleLabel.setOpaque(true);
		titleLabel.setDisabledTextColor(Color.black);
		titleLabel.setHorizontalAlignment(JTextField.CENTER);
		mainPanel.add(titleLabel);
		
		JButton leftArrow = new JButton(ARROWLEFT);
		leftArrow.setBounds(1, 16, 16, 16);
		leftArrow.setBorder(empty);
		leftArrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				transitionMainPanel(CONTACT_SCREEN);
            }
        });
		mainPanel.add(leftArrow);
		
		if (type == "filled") {
			JButton minusButton = new JButton(MINUS);
			minusButton.setBounds(146, 16, 16, 16);
			minusButton.setBorder(empty);
			minusButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					myCont.deleteItem(curCont);
					transitionMainPanel(CONTACT_SCREEN);
				}
			});
			mainPanel.add(minusButton);
		}
		
		JButton editButton = new JButton(type == "blank" ? PLUS : EDIT);
		editButton.setBounds(166, 16, 16, 16);
		editButton.setBorder(empty);
		editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
				// Commit the edit here
				if (type == "blank") {
					contactCommitAdd();
				} else {
					contactCommitEdit();
				}
				transitionMainPanel(CONTACT_SCREEN);
            }
        });
		mainPanel.add(editButton);
		
		String theFName = "First Name";
		String theLName = "Last Name";
		String thePhone = "Phone";
		String theMobile = "Mobile";
		String theCompany = "Company";
		String thePicture = "Picture Link";
		x
		if (type == "filled") {
			theFName = curCont.getFName();
			theLName = curCont.getLName();
			thePhone = curCont.getPhone();
			theMobile = curCont.getMobile();
			theCompany = curCont.getCompany();
			thePicture = curCont.getPicture();
		}
		
		fNameField = new JTextField(theFName);
		fNameField.setBounds(3, 50, 182, 24);
		fNameField.setFont(new Font("Arial", Font.PLAIN, 12));
		mainPanel.add(fNameField);
		
		lNameField = new JTextField(theLName);
		lNameField.setBounds(3, 78, 182, 24);
		lNameField.setFont(new Font("Arial", Font.PLAIN, 12));
		mainPanel.add(lNameField);
		
		phoneField = new JTextField(thePhone);
		phoneField.setBounds(3, 106, 182, 24);
		phoneField.setFont(new Font("Arial", Font.PLAIN, 12));
		mainPanel.add(phoneField);
		
		mobileField = new JTextField(theMobile);
		mobileField.setBounds(3, 134, 182, 24);
		mobileField.setFont(new Font("Arial", Font.PLAIN, 12));
		mainPanel.add(mobileField);
		
		companyField = new JTextField(theCompany);
		companyField.setBounds(3, 162, 182, 24);
		companyField.setFont(new Font("Arial", Font.PLAIN, 12));
		mainPanel.add(companyField);
		
		pictureField = new JTextField(thePicture);
		pictureField.setBounds(3, 190, 182, 24);
		pictureField.setFont(new Font("Arial", Font.PLAIN, 12));
		mainPanel.add(pictureField);
	}
	
	private void contactCommitEdit() {
		curCont.updateAll(fNameField.getText(), lNameField.getText(), phoneField.getText(), mobileField.getText(), companyField.getText(), pictureField.getText());
	}
	
	private void contactCommitAdd() {
		myCont.addContact(fNameField.getText(), lNameField.getText(), phoneField.getText(), mobileField.getText(), companyField.getText(), pictureField.getText());
	}
	
	private void transitionMainPanel(int newPanel) {
		clearMainPanel();
		switch (newPanel) {
			case MAIN_SCREEN: populateMain(); break;
			case CALENDAR_SCREEN: populateCalendar(); break;
			case CALENDAR_EDIT: populateCalEdit("filled"); break;
			case CALENDAR_ADD: populateCalEdit("blank"); break;
			case CONTACT_SCREEN: populateContact(); break;
			case CONTACT_EDIT: populateContEdit("filled"); break;
			case CONTACT_ADD: populateContEdit("blank"); break;
			default: System.exit(1);
		}
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	private void changeDates(long change) {
		curTime = curTime + change;
		Date curDate = new Date(curTime);
		day = curDate.toString().substring(8, 10);
		curDay = Integer.parseInt(day);
		curMonth = Integer.parseInt(curDate.toString().substring(5, 7));
		month = months.get(curMonth);
	}
	
	private void clearMainPanel() {
		mainPanel.removeAll();
	}
	
	private void showMsgBox() {
		msg.setVisible(true);
	}
	private void hideMsgBox() {
		msg.setVisible(false);
	}
	
	private void changeQuote() {
		if (quoteNum == 3) {
			arrowRight.setVisible(false);
		}
		msgArea.setText("Thought for the Day:\n" + getNewQuote());
	}
	
	private String getNewQuote() {
		String quote = quotes.getRandomQuote();
		return quote;
	}
	
	private void clearContAction(ActionEvent evt) {
		myCont.deleteAll();
		transitionMainPanel(CALENDAR_SCREEN);
	}
	
	private void clearCalAction(ActionEvent evt) {
		myCal.deleteAll();
		transitionMainPanel(CONTACT_SCREEN);
	}
	
	private void exitAction(ActionEvent evt) {
		closeApp();
	}
	
	private void closeApp() {
		System.exit(0);
	}
	
	public static void main(String[] args) {
		JFrame frame = new PersOrg();
		frame.setSize(188, 264);
		frame.setVisible(true);
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}