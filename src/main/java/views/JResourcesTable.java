package views;

import javax.swing.JTable;

public class JResourcesTable  extends JTable{
	
	private static final long serialVersionUID = 1L;
	
	//contient le modele 	 
	private JTableRessourceModel tableModel;	
	
	public JResourcesTable(JTableRessourceModel tableModel){
		//super(passModel);
		this.setTableModel(tableModel);
	}

	public JTableRessourceModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(JTableRessourceModel tableModel) {
		this.tableModel = tableModel;
	}
}
