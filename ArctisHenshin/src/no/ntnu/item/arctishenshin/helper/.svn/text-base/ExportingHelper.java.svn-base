package no.ntnu.item.arctishenshin.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import no.ntnu.item.arctis.BuildingBlockActionHelper;
import no.ntnu.item.ramses.integration.EMFHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.model.Graph;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.TransformationSystem;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;

import arctishenshin.ArctisHenshinHelper;
import de.tub.tfs.henshin.editor.layout.HenshinLayoutFactory;
import de.tub.tfs.henshin.editor.layout.LayoutSystem;

public class ExportingHelper {
//	public static final String fileExtension = MuvitorActivator.getUniqueExtensionAttributeValue(
//			    "org.eclipse.ui.editors", "extensions");

	public static String UML_URI="http://www.eclipse.org/uml2/3.0.0/UML";
	  
	public static EObject export(NamedElement element, String value,IProject project,
			IProgressMonitor monitor,final IWorkbenchPart targetPart) {
		TransformationSystem transSys = null;
		LayoutSystem layoutSystem=null;
		
	    // create a graph on this *.henshin file as the working 
	    // graph for the transformation system
	    Resource transformationResource= ArctisHenshinHelper.ensureResourceForTransformationSystemExists(project, value, monitor);
	    Resource layoutResource=ArctisHenshinHelper.ensureResourceForLayoutExists(project, value, monitor);
	    
	    BuildingBlockActionHelper.setupJavaProject(project, monitor);
	    class Runnable1 implements Runnable{
	    	TransformationSystem transSys;
	    	Resource transformationResource;
	    	LayoutSystem layoutSystem;
	    	Resource layoutRes;
	    	NamedElement element;
	    	public Runnable1 (TransformationSystem transSys,Resource transformationResource,LayoutSystem layoutSystem,
	    			Resource layoutRes,IProject project,NamedElement element,IProgressMonitor monitor){
	    		this.transSys=transSys;
	    		this.element=element;
	    		this.transformationResource=transformationResource;
	    		this.layoutRes=layoutRes;
	    		this.layoutRes=layoutRes;
	    	}
			@Override
			public void run() {
				//create the transformation system and save its resources
			    transSys=(TransformationSystem)ExportingHelper.createDefaultModel();
			    org.eclipse.emf.ecore.EPackage ePackage=EPackage.Registry.INSTANCE.getEPackage((String)UML_URI);

			    transSys.getImports().add(ePackage);
			    
				
				//create the layout system
				
				try {
					layoutSystem=HenshinLayoutFactory.eINSTANCE.createLayoutSystem();
//					HenshinLayoutFactory hlfi=(HenshinLayoutFactory) EPackage.Registry.INSTANCE.getEFactory("http://de.tub.tfs.henshin.editor.layout");
//					layoutSystem = hlfi.createLayoutSystem();
					if(layoutSystem ==null){
						System.out.println(HenshinLayoutFactory.eINSTANCE.equals(null)+"the hanshinlayout factory is not reigstered.");
						layoutSystem=HenshinLayoutFactory.eINSTANCE.createLayoutSystem();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			    layoutRes.getContents().clear();
			    
			    //create graph for building block
			    ArctisHenshinHelper.createGraphContent(transSys,layoutSystem,element);		
			    //export the state space to the shenshin 
			    ArctisHenshinHelper.createSSGraphContent(transSys, layoutSystem, element,targetPart);
			    
			    layoutRes.getContents().add(layoutSystem);
			    transformationResource.getContents().add(transSys);
			    
			    Map<String, Boolean> options1 = new HashMap<String, Boolean>();
			    options1.put("DECLARE_XML", Boolean.TRUE);
		        Map<String, String> options = new HashMap<String, String>();
		        options.put(XMLResource.OPTION_ENCODING,"utf-8");
			    try {
			    	System.out.println("trying to save the henshin and layout file.");
					transformationResource.save(options);
					layoutRes.save(options1);
					System.out.println("success............");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
	    }
	    //transformation system
	    Runnable r=new Runnable1(transSys,transformationResource,layoutSystem,layoutResource,project,element, monitor);
	    try {
			EMFHelper.execute(r, transformationResource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return transSys;
	}

	protected static EObject createDefaultModel() {
		HenshinFactory modelFactory = HenshinFactory.eINSTANCE;
		TransformationSystem model = modelFactory.createTransformationSystem();
		model.setName("TransSys for Arctis Building Block");
		return model;
	}
//	protected static EObject createLayoutModel(){
//		EObject layout=null;
//		return layout;
//	}
}
