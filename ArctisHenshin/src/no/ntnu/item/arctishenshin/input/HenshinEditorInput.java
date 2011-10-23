package no.ntnu.item.arctishenshin.input;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.model.TransformationSystem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class HenshinEditorInput implements IEditorInput{
	private EObject transSys;
	public HenshinEditorInput(EObject transforSystem){
	    assert (transSys != null);
	    this.transSys = transforSystem;
	}
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
	    if ((this.transSys instanceof TransformationSystem)) return ((TransformationSystem)this.transSys).getName();
	    return this.transSys.toString();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
	    return getName();
	}
	  public boolean equals(Object other) {
		    if (other == null) {
		      return false;
		    }
		    if (this.transSys == null) {
		      return false;
		    }
		    if ((other instanceof HenshinEditorInput)) {
		      if (((HenshinEditorInput)other).transSys == null) {
		        return false;
		      }
		      return this.transSys.equals(((HenshinEditorInput)other).transSys);
		    }
		    return super.equals(other);
		  }

}
