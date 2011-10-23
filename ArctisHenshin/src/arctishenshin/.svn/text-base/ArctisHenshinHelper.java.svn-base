package arctishenshin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import no.ntnu.item.arctis.analysis.core.IActivityStep;
import no.ntnu.item.arctis.analysis.core.specificationinstances.FEdge;
import no.ntnu.item.arctis.analysis.core.specificationinstances.FLocation;
import no.ntnu.item.arctis.analysis.core.specificationinstances.FNode;
import no.ntnu.item.arctis.analysis.core.specificationinstances.FTransferEdge;
import no.ntnu.item.arctis.analysis.core.statespace.State;
import no.ntnu.item.arctis.analysis.core.statespace.StateSpace;
import no.ntnu.item.arctis.analysis.core.statespace.StepInstance;
import no.ntnu.item.arctis.analysis.ui.internal.Analysis;
import no.ntnu.item.arctis.bbmodel.ABlock;
import no.ntnu.item.arctis.bbmodel.AModel;
import no.ntnu.item.arctis.guihelper.list.IItem;
import no.ntnu.item.arctis.inspection.IItemCollector;
import no.ntnu.item.arctis.inspection.NewInspector;
import no.ntnu.item.arctis.inspection.ResultRegister;
import no.ntnu.item.arctis.inspection.results.BlockContainer;
import no.ntnu.item.arctishenshin.factory.TransformationSysFactory;
import no.ntnu.item.ramses.integration.EMFHelper;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.henshin.model.Graph;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.henshin.model.TransformationSystem;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.uml2.uml.AcceptEventAction;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityFinalNode;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.ActivityPartition;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.CallBehaviorAction;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Collaboration;
import org.eclipse.uml2.uml.CollaborationUse;
import org.eclipse.uml2.uml.ControlFlow;
import org.eclipse.uml2.uml.DecisionNode;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.FlowFinalNode;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.InitialNode;
import org.eclipse.uml2.uml.JoinNode;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.ObjectFlow;
import org.eclipse.uml2.uml.Pin;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Variable;


import de.tub.tfs.henshin.editor.layout.LayoutSystem;

public class ArctisHenshinHelper {
	public static String HENSHIN_POSTFIX=".henshin"; 
	public static String HENSHINLAYOUT_POSTFIX=".henshinlayout";
	public static String UML_URI="http://www.eclipse.org/uml2/3.0.0/UML";
	public static Resource ensureResourceForTransformationSystemExists(
			IProject arctisProject, String buildingBlockName,IProgressMonitor monitor) {
		   IFile resourceFile = ArctisHenshinHelper.createHenshinFile(buildingBlockName,arctisProject,monitor);

		    if (!resourceFile.exists()) {
		    	System.out.println("create resource File failure.");
		    }
		    URI uri = URI.createPlatformResourceURI(resourceFile.getFullPath().toString(), false);
		    ResourceSet set = EMFHelper.getWorkspaceMasterEditingDomain().getResourceSet();
		    Resource r = set.getResource(uri, false);
		    if (r != null) {
		      return r;
		    }
		    Resource transformationResource = EMFHelper.getWorkspaceMasterEditingDomain().getResourceSet().createResource(uri);
		    try {
		        Map<String, String> options = new HashMap<String, String>();
		        options.put(XMLResource.OPTION_ENCODING,"utf-8");
		        transformationResource.save(options);
		    	transformationResource.load(null);
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    return transformationResource;
	}
	public static Resource ensureResourceForLayoutExists(IProject arctisProject, String buildingBlockName,IProgressMonitor monitor) {
		   IFile resourceFile = ArctisHenshinHelper.createHenshinLayoutFile(buildingBlockName,arctisProject,monitor);

		    if (!resourceFile.exists()) {
		    	System.out.println("create layout resource File failure.");
		    }
		    URI uri = URI.createPlatformResourceURI(resourceFile.getFullPath().toString(), false);
		    ResourceSet set = EMFHelper.getWorkspaceMasterEditingDomain().getResourceSet();
		    Resource r = set.getResource(uri, false);
		    
		    
		    if (r != null) {
		      return r;
		    }
		    Resource transformationResource = EMFHelper.getWorkspaceMasterEditingDomain().getResourceSet().createResource(uri);
		    try {
		        transformationResource.save(null);
		    	transformationResource.load(null);
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    return transformationResource;
	}
	private static IFile createHenshinFile(String buildingblockName, IProject project,
			IProgressMonitor monitor) {
		String name="";
	    if(!buildingblockName.contains(".henshin"))
	    	name=buildingblockName+HENSHIN_POSTFIX;
	    else name=buildingblockName;
	    System.out.println("create henshin file with name:"+name);
		IFile transformationFile = null;
		if (!project.getFile(name).equals(null))

			transformationFile = project.getFile(name); // such as file.exists()
														// == false
		String contents = "";
		InputStream source = new ByteArrayInputStream(contents.getBytes());
		try {
			transformationFile.create(source, false, monitor);
			source.close();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return transformationFile;
	}
	private static IFile createHenshinLayoutFile(String buildingblockName,IProject project,IProgressMonitor monitor){
		String name="";
	    if(!buildingblockName.contains(".henshinlayout"))
	    	name=buildingblockName+HENSHINLAYOUT_POSTFIX;
	    else name=buildingblockName;
	    System.out.println("create henshin layout file with name:"+name);
		IFile transformationFile = null;
		if (!project.getFile(name).equals(null))

			transformationFile = project.getFile(name); // such as file.exists()
														// == false
		String contents = "";
		InputStream source = new ByteArrayInputStream(contents.getBytes());
		try {
			transformationFile.create(source, false, monitor);
			source.close();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return transformationFile;
	}
	public static Graph createBuildingBlockGraph(TransformationSystem transSys,
			String value) {
		Graph graph=HenshinFactory.eINSTANCE.createGraph();
		graph.setName(value);
		transSys.getInstances().add(graph);
		return graph;
	}
	public static void createGraphContent(TransformationSystem transSys, LayoutSystem layoutSystem, NamedElement element) {
		HashMap<String,Node> flowMap=new HashMap<String,Node>();
	    Graph buildingBlockGraph = ArctisHenshinHelper.createBuildingBlockGraph(transSys, element.getName());
	    Graph structure=ArctisHenshinHelper.createBuildingBlockGraph(transSys, "structure");
		try {
			EList<Element> activityBehavior=null;	
			if(element instanceof Collaboration){
			Collaboration collaboration=(Collaboration)element;
			Node cNode=TransformationSysFactory.createCollaborationNode(structure, collaboration,layoutSystem);
			activityBehavior= collaboration.getClassifierBehavior().getOwnedElements();
			
			EList<CollaborationUse> cus=collaboration.getCollaborationUses();
			for(Iterator<CollaborationUse> it=cus.iterator();it.hasNext();){
				CollaborationUse acu=it.next();
				TransformationSysFactory.createCollaborationUse(structure,cNode,acu,layoutSystem);
			}
			
			
			} else if (element instanceof Activity){
				Activity act=(Activity)element;
				TransformationSysFactory.createActivityNode(structure,act,layoutSystem);
				activityBehavior= act.getOwnedElements();
			}
			if (activityBehavior != null) {
				for (Element activityBehav : activityBehavior) {
					NamedElement ele=(NamedElement)activityBehav;
					
					if (ele instanceof ActivityPartition) {
						TransformationSysFactory.createActivityPartitionNode(buildingBlockGraph,ele,layoutSystem);
					}
					if (ele instanceof AcceptEventAction) {
						AcceptEventAction aea= (AcceptEventAction)ele;
						//create timer node
						if(aea.getName().contains("t"))
							TransformationSysFactory.createTimerNode(buildingBlockGraph, ele, layoutSystem,flowMap);
						//create receive message node
						else if(aea.getName().contains("r"))
							TransformationSysFactory.createAcceptEvent(buildingBlockGraph, ele, layoutSystem,flowMap);
					}
					if (ele instanceof StateMachine) {
						Graph esm=ArctisHenshinHelper.createBuildingBlockGraph(transSys, ele.getName()+"_esm");
						TransformationSysFactory.createStateMachineNode(esm, ele, layoutSystem);
					}

					if (ele instanceof CallOperationAction) {
						TransformationSysFactory.createCallOperationAtion(buildingBlockGraph, ele, layoutSystem,flowMap);
					}
					if (ele instanceof ActivityParameterNode) {
						TransformationSysFactory.createActivityParameterNode(buildingBlockGraph, ele, layoutSystem,flowMap);
						

					}

					if(ele instanceof CallBehaviorAction){
					    Graph esm=ArctisHenshinHelper.createBuildingBlockGraph(transSys, ele.getName()+"_esm");
						TransformationSysFactory.createCallBehaviorActionNode(buildingBlockGraph,esm, ele, layoutSystem,flowMap);
						// handle a building block that consists internal building blocks
					}
					if(ele instanceof ForkNode){
						TransformationSysFactory.createForkNode(buildingBlockGraph, ele, layoutSystem,flowMap);
						
					}
					if(ele instanceof MergeNode){
						TransformationSysFactory.createMergeNode(buildingBlockGraph, ele, layoutSystem,flowMap);
					}if(ele instanceof Variable){
						TransformationSysFactory.createVariableNode(buildingBlockGraph, ele, layoutSystem);
					}
					if(ele instanceof DecisionNode){
						TransformationSysFactory.createDecisionNode(buildingBlockGraph, ele, layoutSystem,flowMap);
					}
					if(ele instanceof JoinNode){
						TransformationSysFactory.createJoinNode(buildingBlockGraph, ele, layoutSystem,flowMap);
					}if(ele instanceof InitialNode){
						TransformationSysFactory.createInitialNode(buildingBlockGraph, ele, layoutSystem,flowMap);
					}if(ele instanceof ActivityFinalNode){
						TransformationSysFactory.createActivityFinalNode(buildingBlockGraph, ele, layoutSystem,flowMap);
					}if(ele instanceof FlowFinalNode){
						TransformationSysFactory.createFlowFinalNode(buildingBlockGraph, ele, layoutSystem,flowMap);
					}if(ele instanceof ReadVariableAction){
						TransformationSysFactory.createReadVariableActionNode(buildingBlockGraph, ele, layoutSystem,flowMap);
					}if(ele instanceof AddVariableValueAction){
						TransformationSysFactory.createAddVariableValueActionNode(buildingBlockGraph, ele, layoutSystem,flowMap);
					}
				}
//				for (Element activityBehav : activityBehavior) {	
//					NamedElement ele=(NamedElement)activityBehav;
//					if (ele instanceof ControlFlow) {
//						TransformationSysFactory.createControlFlowNode(buildingBlockGraph, ele, layoutSystem);
//						
//					}
//					if (ele instanceof ObjectFlow) {
//						TransformationSysFactory.createObjectFlowNode(buildingBlockGraph, ele, layoutSystem);
//					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// create a separate graph for state space 
	
	public static void createSSGraphContent(TransformationSystem transSys, LayoutSystem layoutSystem, NamedElement element,IWorkbenchPart targetPart) {
		Activity activity=null;
		if(element instanceof Activity)
			activity=(Activity)element;
		else if(element instanceof Collaboration){
			Collaboration co=(Collaboration)element;
			activity=(Activity)co.getClassifierBehavior();
		}
		else if(element instanceof ABlock){
			activity=(Activity) ((ABlock)element).load();
		}		
		
		
		Graph ssGraph = ArctisHenshinHelper.createBuildingBlockGraph(transSys, activity.getName()+"_StateSpace");
		ABlock ab = AModel.getInstance().findBlock(activity.eResource());
		try {
			class R4SSS implements IRunnableWithProgress {
				ABlock ab;

				public R4SSS(ABlock ab) {
					this.ab = ab;
				}
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					NewInspector.analyzeBlockWhenSaving(ab, monitor);
					monitor.done();
				}
			}
			IRunnableWithProgress progress = new R4SSS(ab);
			try {
				new ProgressMonitorDialog(targetPart.getSite().getShell())
						.run(true, true, progress);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BlockContainer container = ResultRegister.INSTANCE.getContainer(ab);
			if (container.canExecuteAdvanced()) {
			    Analysis analysis = new Analysis();
			    System.out.println("analysis.analyze..........");
			    //it stopped here   i don't know why.............
			    analysis.analyze(activity, new NullProgressMonitor(), new IItemCollector() {
			      public void addItem(IItem item) {
			      }
			      public int size() {
			        return 0;
			      }
			    });
			    
			    if(analysis.getStateSpace()!=null){ 
			    	StateSpace ss= analysis.getStateSpace();
			        HashMap<String,Node> states = new HashMap<String,Node>();	        	
					Collection<FLocation> flocations = analysis
					.getSpecification().getLocations();
					System.out.println("analysis.getStateSpace()!=null..........");

							for(State state:ss.getStates()){
								ArctisHenshinHelper.buildState(state,ssGraph, states);
							}
							System.out.println("state.......");
							for(StepInstance si:ss.getStepInstances()){
								ArctisHenshinHelper.buildTransition(si, ssGraph,states);
							}
							System.out.println("buildTransition.......");
			    }
			  } else {
			    System.out.println("Due to errors in the building block, the ESM cannot be generated.");
			  }			
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void buildState(State state,Graph ssGraph,HashMap<String,Node> states){
		try {
			Node stateNode = TransformationSysFactory.createNode(ssGraph, 
					UMLFactory.eINSTANCE.getUMLPackage().getState(), String.valueOf(state.id));
			
			states.put(String.valueOf(state.id),stateNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void buildTransition(StepInstance si,Graph ssGraph,HashMap<String,Node> states){
		IActivityStep iai=(IActivityStep)si.getStep(); 
		String transitionName="";

		String participantEdge="";
		for(FEdge edge:iai.getParticipatingEdges()){
			if(participantEdge.equals(""))
				participantEdge=edge.getElement().getName();
			else participantEdge+=","+edge.getElement().getName();
		}
		transitionName=transitionName+"participantEdge:"+participantEdge+".";
		String participantNode="";
		String location="";
		for(FNode node:iai.getParticipatingNodes()){
			location=node.getLocation().getElement().getName();
			String nodeName="";
			if(node.getElement() instanceof Pin){
				Pin pin= (Pin)node.getElement();
				NamedElement parent=(NamedElement)pin.getOwner();
				nodeName=parent.getName()+":"+node.getElement().getName();
			}
			else nodeName=node.getElement().getName();
			if(participantNode.equals(""))
				participantNode=nodeName;
			else participantNode+=","+nodeName;
		}
		transitionName=transitionName+"location:"+location+".";		
		transitionName=transitionName+"participantNode:"+participantNode+".";
		transitionName=transitionName+"trigger:"+iai.getTrigger().getElement().getName()+".";
		transitionName=transitionName+"triggerType:"+((iai.getTrigger() instanceof FTransferEdge)?"FTransferEdge":"")+".";		
		
			  Set<FEdge> edges=iai.getParticipatingEdges();
			  Object[] o=iai.getParticipatingNodes().toArray();
			  FNode a =(FNode)o[0];
			  if((iai.getTrigger() instanceof FTransferEdge)){
				String trigger="?"+iai.getTrigger().getElement().getName();
				transitionName=transitionName+"name:"+trigger+".";	
			  } else {
				  boolean set=false;
				  for(FEdge edge:edges){
					  if((edge.getElement() instanceof ControlFlow)&&  (((ControlFlow)edge.getElement()).getInPartitions().size() > 1)
							  ){
						  transitionName=transitionName+"name:"+"!"+((ControlFlow)edge.getElement()).getName()+".";
						  set=true;
						  break;
						  }
					  else if((edge.getElement() instanceof ObjectFlow)&&  (((ObjectFlow)edge.getElement()).getInPartitions().size() > 1)
							  ){
						  transitionName=transitionName+"name:"+"!"+((ObjectFlow)edge.getElement()).getName()+".";
						  transitionName=transitionName+"error:"+".";
						  
						  set=true;
						  break;
					  }
				  }
				  if(!set)
					  transitionName=transitionName+"name:"+".";
			  }
			  if(iai.isErroneous())
				  transitionName=transitionName+"error:"+"yes.";
			  else transitionName=transitionName+"error:"+"no.";
				Node transitionNode=TransformationSysFactory.createNode(ssGraph,UMLFactory.eINSTANCE.getUMLPackage().getTransition(),transitionName);
			  TransformationSysFactory.createEdge(ssGraph,UMLFactory.eINSTANCE.getUMLPackage().getTransition_Source(), transitionNode, 
					states.get(String.valueOf(si.getSource().id)));
			  TransformationSysFactory.createEdge(ssGraph,UMLFactory.eINSTANCE.getUMLPackage().getTransition_Target(), transitionNode, 
					states.get(String.valueOf(si.getTarget().id)));		
			  
			System.out.println("create transition: s:"+String.valueOf(si.getSource().id)+" t:"+String.valueOf(si.getTarget().id)+transitionName);
	}
}
