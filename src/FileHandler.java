import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileHandler {

	private FileNameExtensionFilter datfilter = new FileNameExtensionFilter("dat files (*.dat)", "dat");
	//file passed in
	//String generatedFileName; passed in
	//change passed in
	//pass aplication in
	//pass in ed object
	File file;
	String generatedFileName;
	boolean change;
	RandomFile application;
	EmployeeDetails employeeDetails;
	Employee currentEmployee;
	

	public FileHandler(File file, String generatedFileName, boolean change, RandomFile application,
			EmployeeDetails employeeDetails, Employee currentEmployee) {
		this.file = file;
		this.generatedFileName = generatedFileName;
		this.change = change;
		this.application = application;
		this.employeeDetails = employeeDetails;
		this.currentEmployee = currentEmployee;
	}
	
	


	public File getFile() {
		return file;
	}
	
	public String getFilePath() {
		return file.getAbsolutePath();
	}

	public Employee getCurrentEmployee() {
		return currentEmployee;
	}

		// open file
		protected File openFile(JTextField idField,long currentByteStart) {
			System.out.println(employeeDetails);
			final JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open");
			// display files in File Chooser only with extension .dat
			fc.setFileFilter(datfilter);
			File newFile; // holds opened file name and path
			// if old file is not empty or changes has been made, offer user to save
			// old file
			if (file.length() != 0 || change) {
				//frame maybe wrong
				int returnVal = JOptionPane.showOptionDialog(EmployeeDetails.frame, "Do you want to save changes?", "Save",
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
				// if user wants to save file, save it
				if (returnVal == JOptionPane.YES_OPTION) {
					saveFile(idField,currentByteStart);// save file
				} // end if
			} // end if

			int returnVal = fc.showOpenDialog(employeeDetails);
			// if file been chosen, open it
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				newFile = fc.getSelectedFile();
				// if old file wasn't saved and its name is generated file name,
				// delete this file
				if (file.getName().equals(generatedFileName))
					file.delete();// delete file
				file = newFile;// assign opened file to file
				// open file for reading
				application.openReadFile(file.getAbsolutePath());
				employeeDetails.firstRecord();// look for first record
				employeeDetails.displayRecords(currentEmployee);
				application.closeReadFile();// close file for reading
			} // end if
			return file;
		}// end openFile
		
		// save file
		protected void saveFile(JTextField idField,long currentByteStart) {
			// if file name is generated file name, save file as 'save as' else save
			// changes to file
			if (file.getName().equals(generatedFileName))
				saveFileAs();// save file as 'save as'
			else {
				// if changes has been made to text field offer user to save these
				// changes
				if (change) {
					int returnVal = JOptionPane.showOptionDialog(EmployeeDetails.frame, "Do you want to save changes?", "Save",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
					// save changes if user choose this option
					if (returnVal == JOptionPane.YES_OPTION) {
						// save changes if ID field is not empty
						if (!idField.getText().equals("")) {
							// open file for writing
							application.openWriteFile(file.getAbsolutePath());
							// get changes for current Employee
							currentEmployee = employeeDetails.getChangedDetails();
							// write changes to file for corresponding Employee
							// record
							application.changeRecords(currentEmployee, currentByteStart);
							application.closeWriteFile();// close file for writing
						} // end if
					} // end if
				} // end if
				//not displaying current refactor
				employeeDetails.displayRecords(currentEmployee);
				employeeDetails.setEnabled(false);
			} // end else
		}// end saveFile
		
		// save file as 'save as'
		protected File saveFileAs() {
			final JFileChooser fc = new JFileChooser();
			File newFile;
			String defaultFileName = "new_Employee.dat";
			fc.setDialogTitle("Save As");
			// display files only with .dat extension
			fc.setFileFilter(datfilter);
			fc.setApproveButtonText("Save");
			fc.setSelectedFile(new File(defaultFileName));

			int returnVal = fc.showSaveDialog(employeeDetails);
			// if file has chosen or written, save old file in new file
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				newFile = fc.getSelectedFile();
				// check for file name
				if (!checkFileName(newFile)) {
					// add .dat extension if it was not there
					newFile = new File(newFile.getAbsolutePath() + ".dat");
					// create new file
					application.createFile(newFile.getAbsolutePath());
				} // end id
				else
					// create new file
					application.createFile(newFile.getAbsolutePath());

				try {// try to copy old file to new file
					Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					// if old file name was generated file name, delete it
					if (file.getName().equals(generatedFileName))
						file.delete();// delete file
					file = newFile;// assign new file to file
				} // end try
				catch (IOException e) {
				} // end catch
			} // end if
			employeeDetails.setChangesMade(false);
			return file;
		}// end saveFileAs
		
		// check if file name has extension .dat
		protected boolean checkFileName(File fileName) {
			boolean checkFile = false;
			int length = fileName.toString().length();

			// check if last characters in file name is .dat
			if (fileName.toString().charAt(length - 4) == '.' && fileName.toString().charAt(length - 3) == 'd'
					&& fileName.toString().charAt(length - 2) == 'a' && fileName.toString().charAt(length - 1) == 't')
				checkFile = true;
			return checkFile;
		}// end checkFileName

		// generate 20 character long file name
		private String getFileName() {
			String fileNameChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
			StringBuilder fileName = new StringBuilder();
			Random rnd = new Random();
			// loop until 20 character long file name is generated
			while (fileName.length() < 20) {
				int index = (int) (rnd.nextFloat() * fileNameChars.length());
				fileName.append(fileNameChars.charAt(index));
			}
			String generatedfileName = fileName.toString();
			return generatedfileName;
		}// end getFileName
		
		// create file with generated file name when application is opened
		protected File createRandomFile() {
			generatedFileName = getFileName() + ".dat";
			// assign generated file name to file
			file = new File(generatedFileName);
			// create file
			application.createFile(file.getName());
			return file;
		}// end createRandomFile
}
