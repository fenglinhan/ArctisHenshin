package no.ntnu.item.arctishenshin.page;

import java.util.HashSet;
import java.util.Set;
import no.ntnu.item.arctis.guihelper.TitleAreaDialog2;
import no.ntnu.item.arctishenshin.helper.ExportingHelper;
import no.ntnu.item.ramses.guihelper.GUIHelper;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import arctishenshin.ArctisHenshinHelper;

public class HenshinFileCreationDialog extends TitleAreaDialog2 {
	  private Set<IInputValidator> validators = new HashSet();
	  private Set<String> notes = new HashSet();
	  private Text nameText;
	  private String value;
	  private String initialValue;
	  private String shellTitle;
	  private String title;
	
	
	public HenshinFileCreationDialog(String initialString, String shellTitle, String title, Shell shell)
	  {
	    super(shell);
	    assert (initialString != null);
	    this.initialValue = initialString;
	    this.shellTitle = shellTitle;
	    this.title = title;
	    setHelpAvailable(false);
	  }

	  public void addValidator(IInputValidator validator) {
	    this.validators.add(validator);
	  }

	  public String getValue() {
	    return this.value;
	  }

	  public void addNote(String note) {
	    this.notes.add(note);
	  }

	  protected Control createDialogArea(Composite parent)
	  {
	    setTitle(this.title);
	    getShell().setText(this.shellTitle);
	    Composite area = (Composite)super.createDialogArea(parent);
	    Composite container = new Composite(area, 0);
	    container.setLayoutData(new GridData(1808));
	    GridLayout g = new GridLayout(2, false);
	    g.marginLeft = 0; g.marginRight = 0;
	    container.setLayout(g);

	    Label label = new Label(container, 0);
	    label.setText("Transformation file name:");
	    GridData gd = new GridData();
	    gd.horizontalIndent = 2;
	    gd.horizontalSpan = 1;
	    label.setLayoutData(gd);
	    Dialog.applyDialogFont(label);

	    this.nameText = new Text(container, 2052);
	    gd = new GridData(768);
	    gd.horizontalSpan = 1;
	    gd.widthHint = 350;
	    this.nameText.setLayoutData(gd);
	    this.nameText.setFocus();
	    this.nameText.addModifyListener(this);
	    this.nameText.setText(this.initialValue+ArctisHenshinHelper.HENSHIN_POSTFIX);

	    for (String note : this.notes)
	    {
	      GUIHelper.placeholder(container, 1);

	      Composite box1 = GUIHelper.createInfoMessageComposite(container, note);
	      gd = new GridData();
	      gd.horizontalSpan = 1;
	      gd.horizontalIndent = 2;
	      box1.setLayoutData(gd);
	    }

	    dialogChanged();

	    return area;
	  }

	  protected void dialogChanged() {
	    this.value = this.nameText.getText();
	    for (IInputValidator v : this.validators) {
	      String result = v.isValid(this.value);
	      if (result != null) {
	        addError(result);
	      }
	    }
	    displayMostSevere();
	  }

	}