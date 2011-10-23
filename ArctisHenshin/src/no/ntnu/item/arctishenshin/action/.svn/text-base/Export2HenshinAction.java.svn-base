package no.ntnu.item.arctishenshin.action;

import java.lang.reflect.InvocationTargetException;
import no.ntnu.item.arctis.activities.edit.RenamingHelper;
import no.ntnu.item.arctis.bbmodel.ABlock;
import no.ntnu.item.arctishenshin.helper.ExportingHelper;
import no.ntnu.item.ramses.integration.EMFHelper;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.uml2.uml.NamedElement;


public class Export2HenshinAction extends Action
implements IObjectActionDelegate, IViewActionDelegate{
	IWorkbenchPart targetPart;
	private IStructuredSelection selection;
	NamedElement element;
	IProject project;
	@Override
	public void run(IAction action) {
	    PlatformUI.getWorkbench().saveAllEditors(true);
	    Object o = this.selection.getFirstElement();
	    NamedElement element = (o instanceof NamedElement) ? (NamedElement)o : ((ABlock)o).load();
	    System.out.println("URI: " + EcoreUtil.getURI(element).fileExtension());
	    System.out.println("URI: " + EcoreUtil.getURI(element).fragment());
	    System.out.println(" ID: " + EcoreUtil.getID(element));
	    System.out.println("IDentification: " + EcoreUtil.getIdentification(element));

	    boolean allClosed = RenamingHelper.closeAllArctisEditors();
	    if (!allClosed) {
	      return;
	    }
	    class Runnable1 implements IRunnableWithProgress{
	    	NamedElement element;
	    	EObject transformationSystem;
	    	public Runnable1(NamedElement element){
	    		this.element=element;
	    	}
			@Override
		      public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					transformationSystem=ExportingHelper.export(element, element.getName(),project, monitor,targetPart);
		      }
			public EObject getTransSys(){
				return transformationSystem;
			}
	    }
	    
	    IRunnableWithProgress runnable = new Runnable1(element);
	    ProgressMonitorDialog pmd = new ProgressMonitorDialog(new Shell());
	    pmd.open();
	    try {

	      pmd.run(true, false, runnable);
//	      EObject transSys=((Runnable1)runnable).getTransSys();
//	      HenshinEditorInput input= new HenshinEditorInput(transSys);
//	      
//	      IWorkbenchPage page = Activator.getDefault().getWorkbench().getWorkbenchWindows()[0].getPages()[0];
//	      if (page != null) {
//	    	  
////		        String pluginName = "de.tub.tfs.muvitor";
//		        String pluginName = "de.tub.tfs.henshin.editor";
//		        String exPointID="org.eclipse.ui.editors";
//		        String editorID="";
//		        String attribID="id";
//		        IExtension[] extensions = Platform.getExtensionRegistry().getExtensions(pluginName);
//		        String attrValue = null;
//		        for (IExtension extension : extensions)
//		        {
//		          if (extension.getExtensionPointUniqueIdentifier().equals(exPointID)) {
//		            IConfigurationElement[] confElems = extension.getConfigurationElements();
//		            if (confElems.length != 1) {
//		              System.err.println("The Plugin " + pluginName + 
//		                " does not specify a unique extension with ID " + exPointID);
//		            }
//		            attrValue = confElems[0].getAttribute("id");
//		            if (attrValue != null) break;
//		            System.err.println("The extension " + exPointID + " of the plugin " + pluginName + 
//		              " does not specify a unique attribute with ID " + attribID);
//		            
//		            editorID=attrValue;
//		            System.out.println("editor ID:"+editorID);
//		          }
//		        }
////	        page.openEditor(input, editorID, true);
//	        
//	        IDE.openEditor(page, input, editorID);
//	      }
	      
	    } catch (InvocationTargetException e) {
	      e.printStackTrace();
	    } catch (InterruptedException e) {
	      e.printStackTrace();
		}
	    
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = ((IStructuredSelection) selection);
		Object o = this.selection.getFirstElement();
		element = (o instanceof NamedElement) ? (NamedElement) o
				: ((ABlock) o).load();
		project = EMFHelper.getProject(element);
		
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	@Override
	public void init(IViewPart view) {
		// TODO Auto-generated method stub
		
	}

}
