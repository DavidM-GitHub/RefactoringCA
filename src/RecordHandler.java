import javax.swing.JOptionPane;

public class RecordHandler {
	RandomFile application;
	FileHandler fileHandler;
	long currentByteStart;
	EmployeeDetails employeeDetails;
	Employee currentEmployee;
	
	
	
	public RecordHandler( FileHandler fileHandler, EmployeeDetails employeeDetails) {
		this.application = employeeDetails.application;
		this.fileHandler = fileHandler;
		this.currentByteStart = employeeDetails.currentByteStart;
		this.employeeDetails = employeeDetails;
		this.currentEmployee = employeeDetails.currentEmployee;
	}

	// add Employee object to fail
		public long addRecord(Employee newEmployee) {
			// open file for writing
			application.openWriteFile(fileHandler.getFilePath());
			// write into a file
			currentByteStart = application.addRecords(newEmployee);
			application.closeWriteFile();// close file for writing
			return currentByteStart;
		}// end addRecord
		
		// delete (make inactive - empty) record from file
		protected void deleteRecord() {
			if (employeeDetails.isSomeoneToDisplay()) {// if any active record in file display
										// message and delete record
				int returnVal = JOptionPane.showOptionDialog(EmployeeDetails.frame, "Do you want to delete record?", "Delete",
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
				// if answer yes delete (make inactive - empty) record
				if (returnVal == JOptionPane.YES_OPTION) {
					// open file for writing
					application.openWriteFile(fileHandler.getFilePath());
					// delete (make inactive - empty) record in file proper position
					System.out.println(currentByteStart);

					application.deleteRecords(currentByteStart);
					application.closeWriteFile();// close file for writing
					// if any active record in file display next record
					if (employeeDetails.isSomeoneToDisplay()) {
						nextRecord();// look for next record
						employeeDetails.displayRecords(currentEmployee);
					} // end if
				} // end if
			} // end if
		}// end deleteDecord
		
		// find byte start in file for last active record
		protected long lastRecord() {
			// if any active record in file look for first record
			if (employeeDetails.isSomeoneToDisplay()) {
				// open file for reading
				application.openReadFile(fileHandler.getFilePath());
				// get byte start in file for last record
				currentByteStart = application.getLast();
				// assign current Employee to first record in file
				currentEmployee = application.readRecords(currentByteStart);
				application.closeReadFile();// close file for reading
				// if last record is inactive look for previous record
				if (currentEmployee.getEmployeeId() == 0)
					previousRecord();// look for previous record
			} // end if
			return currentByteStart;
		}// end lastRecord
		
		// find byte start in file for next active record
		protected long nextRecord() {
			// if any active record in file look for first record
			if (employeeDetails.isSomeoneToDisplay()) {
				// open file for reading
				application.openReadFile(fileHandler.getFilePath());
				// get byte start in file for next record
				currentByteStart = application.getNext(currentByteStart);
				// assign current Employee to record in file
				currentEmployee = application.readRecords(currentByteStart);
				// loop to previous next until Employee is active - ID is not 0
				while (currentEmployee.getEmployeeId() == 0) {
					// get byte start in file for next record
					currentByteStart = application.getNext(currentByteStart);
					// assign current Employee to next record in file
					currentEmployee = application.readRecords(currentByteStart);
				} // end while
				application.closeReadFile();// close file for reading
			} // end if
			return currentByteStart;
		}// end nextRecord
		
		// find byte start in file for first active record
		protected long firstRecord() {
			// if any active record in file look for first record
			if (employeeDetails.isSomeoneToDisplay()) {
				// open file for reading
				application.openReadFile(fileHandler.getFilePath());
				// get byte start in file for first record
				currentByteStart = application.getFirst();
				// assign current Employee to first record in file
				currentEmployee = application.readRecords(currentByteStart);
				application.closeReadFile();// close file for reading
				// if first record is inactive look for next record
				if (currentEmployee.getEmployeeId() == 0)
					nextRecord();// look for next record
			} // end if
			return currentByteStart;
		}// end firstRecord

		// find byte start in file for previous active record
		protected long previousRecord() {
			// if any active record in file look for first record
			if (employeeDetails.isSomeoneToDisplay()) {
				// open file for reading
				application.openReadFile(fileHandler.getFilePath());
				// get byte start in file for previous record
				currentByteStart = application.getPrevious(currentByteStart);
				// assign current Employee to previous record in file
				currentEmployee = application.readRecords(currentByteStart);
				// loop to previous record until Employee is active - ID is not 0
				while (currentEmployee.getEmployeeId() == 0) {
					// get byte start in file for previous record
					currentByteStart = application.getPrevious(currentByteStart);
					// assign current Employee to previous record in file
					currentEmployee = application.readRecords(currentByteStart);
				} // end while
				application.closeReadFile();// close file for reading
			}
			return currentByteStart;
		}// end previousRecord

		public Employee getCurrentEmployee() {
			return currentEmployee;
		}

		
		
}
