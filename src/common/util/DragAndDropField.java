package common.util;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.swing.JTextField;

public class DragAndDropField extends JTextField {

	public DragAndDropField(String text) {
		this();
		this.setText(text);
	}
	
	public DragAndDropField() {
		this.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						// process files
						System.out.println(file.getAbsolutePath());
					}					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

}