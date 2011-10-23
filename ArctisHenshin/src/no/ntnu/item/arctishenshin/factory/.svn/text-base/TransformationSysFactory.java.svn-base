package no.ntnu.item.arctishenshin.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import no.ntnu.item.arctis.ArctisProfileHelper;
import no.ntnu.item.ramses.graphicprofile.GraphicProfileHelper;
import no.ntnu.item.ramses.graphicprofile.GraphicProfileHelper.Orientation;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.henshin.model.Edge;
import org.eclipse.emf.henshin.model.Graph;
import org.eclipse.emf.henshin.model.HenshinFactory;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.uml2.uml.AcceptEventAction;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityParameterNode;
import org.eclipse.uml2.uml.ActivityPartition;
import org.eclipse.uml2.uml.AddVariableValueAction;
import org.eclipse.uml2.uml.CallBehaviorAction;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Collaboration;
import org.eclipse.uml2.uml.CollaborationUse;
import org.eclipse.uml2.uml.ControlFlow;
import org.eclipse.uml2.uml.DecisionNode;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.FinalNode;
import org.eclipse.uml2.uml.FlowFinalNode;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.InitialNode;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.JoinNode;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.ObjectFlow;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.ParameterSet;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ReadVariableAction;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.Vertex;
import org.eclipse.emf.henshin.model.Attribute;

import de.tub.tfs.henshin.editor.layout.HenshinLayoutFactory;
import de.tub.tfs.henshin.editor.layout.LayoutSystem;
import de.tub.tfs.henshin.editor.layout.NodeLayout;

public class TransformationSysFactory {
	public static int node_color_default=0;
	public static int node_color_blue=1;
	public static int node_height=20;
	public static int node_width=100;
	public static Node createCollaborationNode(Graph graph, Collaboration ele,LayoutSystem layout) {
		Node collaborationInstance = createNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getCollaboration(),ele.getName());
		
		//create class
		Activity activity = (Activity) ele.getClassifierBehavior();
		Node activityNode=createActivityNode(graph, activity, layout);
		
		//create reference
		Edge classifierBehavior=HenshinFactory.eINSTANCE.createEdge();
		classifierBehavior.setType(UMLFactory.eINSTANCE.getUMLPackage().getBehavioredClassifier_ClassifierBehavior());
		classifierBehavior.setGraph(graph);
		classifierBehavior.setSource(collaborationInstance);
		classifierBehavior.setTarget(activityNode);
		
		// create owned property
		EList<Property> roles = ele.getAllAttributes();
		for (int i = 0; i < roles.size(); i++) {
			Property p = (Property) roles.get(i);
			Node property=createNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getProperty(),p.getName());
			createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getCollaboration_CollaborationRole(),collaborationInstance,property);
		}
		return collaborationInstance;
	}
	public static void createCollaborationUse(Graph graph, Node collaboration,CollaborationUse cu, LayoutSystem layout){
		EList<Edge> rolesLink=collaboration.getOutgoing();
		ArrayList<Node> roles=new ArrayList<Node>();
		for(Edge edge:rolesLink){
			if(edge.getType().equals(UMLFactory.eINSTANCE.getUMLPackage().getCollaboration_CollaborationRole()))
					roles.add(edge.getTarget());
		}
		
		Node collaborationUseInstance = createNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getCollaborationUse(),cu.getName());
		System.out.println("--------------  type of collaboration:"
				+ cu.getType().eResource().getURI().toString());
			for (Iterator<Dependency> it = cu.getRoleBindings().iterator(); it.hasNext();) {
				Dependency d = (Dependency) it.next();
				Node depency=createNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getDependency(),d.getSuppliers().get(0).getName());
				createEdge(graph, UMLFactory.eINSTANCE.getUMLPackage().getCollaborationUse_RoleBinding(),
						collaborationUseInstance, depency);
				for (int j = 0; j < roles.size(); j++) {
					System.out.println("dependency client:"
							+ ((NamedElement) d.getClients().get(0)).getName()
							+ " supplier:"
							+ ((NamedElement) d.getSuppliers().get(0))
									.getName());

					if (d.getClient(((Node) roles.get(j)).getName()) != null) {
						createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getDependency_Client(),
								depency,roles.get(j));
						System.out.println("set binding relation client:"
								+ ((Node) roles.get(j)).getName()
								+ " supplier:"
								+ ((NamedElement) d.getSuppliers().get(0))
										.getName());
					}
				}
			}
			System.out.println("created collaboration use: " + cu.getName());
	}
	public static void createEdge(Graph graph,EReference type,Node source,Node target){
		Edge owned=HenshinFactory.eINSTANCE.createEdge();
		owned.setType(type);
		owned.setGraph(graph);
		owned.setSource(source);
		owned.setTarget(target);
	}
	public static void createAttribute(Node node, String value, EAttribute eattribute){
		Attribute attribute = HenshinFactory.eINSTANCE.createAttribute();
		attribute.setType(eattribute);
		attribute.setValue(value);
		attribute.setNode(node);
	}
	public static Node createActivityNode(Graph buildingBlockGraph, Activity act, LayoutSystem layoutSystem) {
		Node acti=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivity(),
				act.getName());

//		EList<ParameterSet> paSets = act.getOwnedParameterSets();
//		for (ParameterSet o : paSets) {
//			ParameterSet ps = (ParameterSet) o;
//			createParameterSetNode(ps, edgraph, owner, helper);
//		}
		return acti;
	}
	public static Node createNode(Graph graph, EClass type,String name){
		Node node=HenshinFactory.eINSTANCE.createNode();
		node.setType(type);
		node.setName(name);
		node.setGraph(graph);
		graph.getNodes().add(node);
		return node;
	}
	public static Node createNode(Graph graph, EClass type,String name,int x, int y,LayoutSystem layoutSystem,int color){
		Node node=HenshinFactory.eINSTANCE.createNode();
		node.setType(type);
		node.setName(name);
		
		NodeLayout layout=HenshinLayoutFactory.eINSTANCE.createNodeLayout();
		layout.setNode(node);
		layout.setX(x);
		layout.setY(y);
		layout.setColor(color);
		layoutSystem.getNodeLayouts().add(layout);
		graph.getNodes().add(node);
		return node;
	}
	
	// create a comment with body and name for the annotated element
	public static void createComment(Node node,String content){
		Attribute attribute=HenshinFactory.eINSTANCE.createAttribute();
		attribute.setNode(node);
		attribute.setType(UMLFactory.eINSTANCE.getUMLPackage().getNamedElement_Name());
		attribute.setValue(content);
//		Node comment=HenshinFactory.eINSTANCE.createNode();
//		comment.setType(UMLFactory.eINSTANCE.getUMLPackage().getComment());
//		comment.setName(name);
//		createAttribute(comment, content, UMLFactory.eINSTANCE.getUMLPackage().getComment_Body());
//		createEdge(graph, UMLFactory.eINSTANCE.getUMLPackage().getComment_AnnotatedElement(), comment,node);
//		comment.setGraph(graph);
//		return attribute;
	}
	
	
	public static void appendComment(Node node, String content){
		EList<Attribute> atts=node.getAttributes();
		for(Attribute at:atts){
			if(at.getType().equals(UMLFactory.eINSTANCE.getUMLPackage().getNamedElement_Name())){
				String value=at.getValue();
				if(value.startsWith(","))
					value=value+content;
				else value=value+","+content;
				at.setValue(value);
			}
		}
	}

	public static void createActivityPartitionNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem) {
		ActivityPartition ap=(ActivityPartition)ele;
		Point point= new Point();
		if (GraphicProfileHelper.hasLocation(ap)){
			 point=GraphicProfileHelper.getLocation(ap);
		}
		Node activityPartitionInstance = createNode(buildingBlockGraph, UMLFactory.eINSTANCE.getUMLPackage().getActivityPartition(), 
				ap.getName(),point.x,point.y,layoutSystem,0);
		Dimension di=GraphicProfileHelper.getSizeIfDefined(ap);
		if(!di.equals(null)){
			createComment(activityPartitionInstance,"height:"+String.valueOf(di.height)+","+"width:"+String.valueOf(di.width));
		}
	}

	public static void createTimerNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		AcceptEventAction aea = (AcceptEventAction) ele;
		Point point= new Point();
		if (GraphicProfileHelper.hasLocation(aea)){
			 point=GraphicProfileHelper.getLocation(aea);
		}
		Node timer=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getAcceptEventAction(),
				aea.getName(),point.x,point.y,layoutSystem,2);
		ActivityPartition partition = (ActivityPartition) aea.getInPartitions()
				.toArray()[0];
		String inPartition="inPartition:"+partition.getName();
		String duration = "duration:"+ArctisProfileHelper.getCurrentValue(aea);
		String value=inPartition+","+duration;
		createComment(timer,value);
		EList<ActivityEdge> inEdges=aea.getIncomings();
		for(ActivityEdge edge:inEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,timer);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,timer);
			}
		}
		EList<ActivityEdge> outEdges=aea.getOutgoings();
		for(ActivityEdge edge:outEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,timer);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,timer);
			}
		}
		
	}

	public static void createAcceptEvent(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		AcceptEventAction aea = (AcceptEventAction) ele;
		Point point= new Point();
		if (GraphicProfileHelper.hasLocation(aea)){
			 point=GraphicProfileHelper.getLocation(aea);
		}
		Node receiveMessageNodeInstance = createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getAcceptEventAction(),
				aea.getName(),point.x,point.y,layoutSystem,3);
		ActivityPartition partition = (ActivityPartition) aea.getInPartitions()
				.toArray()[0];
		String inPartition="inPartition:"+partition.getName();
		String position="x:"+point.x+","+"y:"+point.y;
		createComment(receiveMessageNodeInstance,position+","+inPartition);
		EList<ActivityEdge> inEdges=aea.getIncomings();
		for(ActivityEdge edge:inEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,receiveMessageNodeInstance);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,receiveMessageNodeInstance);
			}
		}
		
		if (aea.getOutputs().toArray().length > 0) {
			OutputPin op = (OutputPin) aea.getOutputs().get(0);
			Node out=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getOutputPin(),op.getName(),point.x,point.y+20,layoutSystem,0);
			String owner="owner:"+aea.getName();
			String typeName="typeName:"+op.getType().getName();
			createComment(out,owner+","+typeName);
			createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getElement_OwnedElement(),receiveMessageNodeInstance,out);
			EList<ActivityEdge> outEdges=op.getOutgoings();
				for(ActivityEdge edge:outEdges){
					if(edge instanceof ControlFlow){
						Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
						createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,out);
					}else if (edge instanceof ObjectFlow){
						Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
						createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,out);
					}
				}
		} else {
			EList<ActivityEdge> outEdges=aea.getOutgoings();
			for(ActivityEdge edge:outEdges){
				if(edge instanceof ControlFlow){
					Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
					createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,receiveMessageNodeInstance);
				}else if (edge instanceof ObjectFlow){
					Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
					createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,receiveMessageNodeInstance);
				}
			}
		}
		
		
		
	}

	public static void createStateMachineNode(Graph esm,
			NamedElement ele, LayoutSystem layoutSystem) {

		EList<Region> regions = ((StateMachine) ele).getRegions();
		System.out.println("get the esm:"+ ((StateMachine) ele).getName());
		Region region = (Region) regions.toArray()[0];
		System.out.println("get the region:" + region.getName());
		HashMap<String,Node> vertexs=new HashMap<String,Node>();
		for (Vertex vertex : region.getSubvertices()) {
			Node vertexInstance = createNode(esm,UMLFactory.eINSTANCE.getUMLPackage().getVertex(),vertex.getName());
			System.out.println("create vertex node:" + vertex.getName());
			vertexs.put(vertex.getName(), vertexInstance);
		}
		for (Transition transition : region.getTransitions()) {
			Node transitionInstance = createNode(esm,UMLFactory.eINSTANCE.getUMLPackage().getTransition(),transition.getName());
			System.out.println("create transition node:"
					+ transition.getName());
			String srcName = "source:"+transition.getSource().getName();
			String tarName = "target:"+transition.getTarget().getName();
			String triggerName = "trigger:"+transition.getName().substring(0,
					transition.getName().indexOf("/"));
			String effectName = "effect:"+transition.getName().substring(
					transition.getName().indexOf("/") + 1,
					transition.getName().length());

			createComment(transitionInstance,srcName+","+tarName+","+triggerName+","+effectName);
			System.out.println("creating transition:"
					+ transition.getName() + " src:" + srcName + " tar:"
					+ tarName);
			createEdge(esm,UMLFactory.eINSTANCE.getUMLPackage().getTransition_Source(),transitionInstance,vertexs.get(transition.getSource().getName()));
			createEdge(esm,UMLFactory.eINSTANCE.getUMLPackage().getTransition_Target(),transitionInstance,vertexs.get(transition.getTarget().getName()));
		}
	}

	public static void createCallOperationAtion(Graph graph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		CallOperationAction co = (CallOperationAction) ele;
		Point point= new Point();
		if (GraphicProfileHelper.hasLocation(co)){
			 point=GraphicProfileHelper.getLocation(co);
		}
		Node callOperationActionInstance = createNode(graph,
				UMLFactory.eINSTANCE.getUMLPackage().getCallOperationAction(),co.getName(),point.x,point.y,layoutSystem,4);

		ActivityPartition partition = (ActivityPartition) ((CallOperationAction) ele)
				.getInPartitions().toArray()[0];
		String position="x:"+String.valueOf(point.x)+","+"y:"+String.valueOf(point.y);
		String inPartition = "inPartition:"+partition.getName();
		String operation="operation:"+co.getOperation().getName();
		createComment(callOperationActionInstance,position+","+inPartition+","+operation);
		
		
		int countParameter=0; 
		int countReturnValue=0;
		EList<Element> parameter = co.getOwnedElements();
		for (int i = 0; i < parameter.size(); i++) {
			if ((parameter.get(i) instanceof InputPin)) {
				InputPin ip = (InputPin) parameter.get(i);

				Node parameterNode = createNode(graph,
						UMLFactory.eINSTANCE.getUMLPackage().getInputPin(),ip.getName(),point.x,point.y+10,layoutSystem,0);
				
				createComment(parameterNode,"");
				EList<ActivityEdge> inEdges=ip.getIncomings();
				for(ActivityEdge edge:inEdges){
					if(edge instanceof ControlFlow){
						Node controFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
						createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,parameterNode);
					}else if (edge instanceof ObjectFlow){
						Node oFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
						createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,parameterNode);
					}
				}
				countParameter++;
			} else {
				if (!(parameter.get(i) instanceof OutputPin))
					continue;
				OutputPin op = (OutputPin) parameter.get(i);
				Node outputPinNode = createNode(graph, 
						UMLFactory.eINSTANCE.getUMLPackage().getOutputPin(),"",point.x,point.y+10,layoutSystem,0);
				String returnValueType = "returnValueType:"+op.getType().getName();
				createComment(outputPinNode,returnValueType);
				EList<ActivityEdge> inEdges=op.getOutgoings();
				for(ActivityEdge edge:inEdges){
					if(edge instanceof ControlFlow){
						Node controFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
						createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,outputPinNode);
					}else if (edge instanceof ObjectFlow){
						Node oFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
						createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,outputPinNode);
					}
				}
				countReturnValue++;
			}
			
		}
		if(countParameter==0){
			EList<ActivityEdge> inEdges=co.getIncomings();
			for(ActivityEdge edge:inEdges){
				if(edge instanceof ControlFlow){
					Node controFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
					createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,callOperationActionInstance);

				}else if (edge instanceof ObjectFlow){
					Node oFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
					createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,callOperationActionInstance);
				}
			}
		}
		if(countReturnValue==0){
			EList<ActivityEdge> inEdges=co.getOutgoings();
			for(ActivityEdge edge:inEdges){
				if(edge instanceof ControlFlow){
					Node controFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
					createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,callOperationActionInstance);
				}else if (edge instanceof ObjectFlow){
					Node oFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
					createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,callOperationActionInstance);
				}
			}
		}
		
	}
	
	public static Node createOrFindFlowNode(Graph graph,EClass eClass, String name,HashMap<String,Node> flowMap){
		if(flowMap.containsKey(name)){
			return flowMap.get(name);
		} else {
			Node node=createNode(graph,eClass,name);
			flowMap.put(name, node);
			return node;
			}
	}

	public static void createControlFlowNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem) {
		ControlFlow cf=(ControlFlow)ele;
		EList<Node> controlNode=buildingBlockGraph.getNodes();
		Node[] nodes=new Node[2];
		int i=0;
		for(Node node:controlNode){
			if(node.getName().equalsIgnoreCase(cf.getName())){
				nodes[i]=node;
				i++;
			}
		}
		
		if((nodes[0]!=null)&&(nodes[1]!=null)){
			Node node1=nodes[0].getOutgoing().get(0).getTarget();
			Node node2=nodes[1].getOutgoing().get(0).getTarget();
			EList<NodeLayout> nls=layoutSystem.getNodeLayouts();
			int x1=0;
			int x2=0;
			int y1=0;
			int y2=0;
			for(NodeLayout nl:nls){
				if(nl.equals(node1)){
					x1=nl.getX();
					y1=nl.getY();
				}
				if(nl.equals(node2)){
					x2=nl.getX();
					y2=nl.getY();
				}
			}
			NodeLayout layout=HenshinLayoutFactory.eINSTANCE.createNodeLayout();
			layout.setNode(nodes[1]);
			layout.setX((x1+x2)/2);
			layout.setY((y1+y2)/2);
			layout.setColor(5);
			layoutSystem.getNodeLayouts().add(layout);
			
			System.err.println(nodes[0].getName()+"          "+nodes[1].getName());
			nodes[0].getOutgoing().get(0).setSource(nodes[1]);
			nodes[0].setName("to_be_delete");
		}
	}

	public static void createObjectFlowNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem) {
		ObjectFlow of=(ObjectFlow)ele;
		EList<Node> objectNode=buildingBlockGraph.getNodes();
		Node[] nodes=new Node[2];
		int i=0;
		for(Node node:objectNode){
			if(node.getName().equalsIgnoreCase(of.getName())){
				nodes[i]=node;
				i++;
			}
		}
		if((nodes[0]!=null)&&(nodes[1]!=null)){
			Node node1=nodes[0].getOutgoing().get(0).getTarget();
			Node node2=nodes[1].getOutgoing().get(0).getTarget();
			EList<NodeLayout> nls=layoutSystem.getNodeLayouts();
			int x1=0;
			int x2=0;
			int y1=0;
			int y2=0;
			for(NodeLayout nl:nls){
				if(nl.equals(node1)){
					x1=nl.getX();
					y1=nl.getY();
				}
				if(nl.equals(node2)){
					x2=nl.getX();
					y2=nl.getY();
				}
			}
			NodeLayout layout=HenshinLayoutFactory.eINSTANCE.createNodeLayout();
			layout.setNode(nodes[1]);
			layout.setX((x1+x2)/2);
			layout.setY((y1+y2)/2);
			layout.setColor(5);
			layoutSystem.getNodeLayouts().add(layout);
			
			nodes[0].getOutgoing().get(0).setSource(nodes[1]);
			nodes[0].setName("to_be_delete");
		}
	}

	public static void createMergeNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		MergeNode merge=(MergeNode)ele;
		Point point=GraphicProfileHelper.getLocation(merge);
		Node mergeNode=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getMergeNode(),merge.getName(),point.x,point.y,layoutSystem, 1);
		EList<ActivityEdge> inEdges=merge.getIncomings();
		for(ActivityEdge edge:inEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,mergeNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,mergeNode);
			}
		}
		EList<ActivityEdge> outEdges=merge.getOutgoings();
		for(ActivityEdge edge:outEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,mergeNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,mergeNode);
			}
		}
		
	}

	public static void createVariableNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem) {
		// TODO Auto-generated method stub
		
	}

	public static void createDecisionNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		DecisionNode decision=(DecisionNode)ele;
		Point point=GraphicProfileHelper.getLocation(decision);
		Node decisionNode=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getForkNode(),decision.getName(),point.x,point.y,layoutSystem, 1);
		EList<ActivityEdge> inEdges=decision.getIncomings();
		for(ActivityEdge edge:inEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,decisionNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,decisionNode);
			}
		}
		EList<ActivityEdge> outEdges=decision.getOutgoings();
		for(ActivityEdge edge:outEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,decisionNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,decisionNode);
			}
		}
		
	}

	public static void createActivityParameterNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		ActivityParameterNode apn=(ActivityParameterNode)ele;
		Point point=GraphicProfileHelper.getLocation(apn);
		Node apNode=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityParameterNode(),apn.getName(),point.x,point.y,layoutSystem, 1);
		
		Activity activity = apn.getActivity();
		EList<Parameter> parameters = activity.getOwnedParameters();

		int i = 0;
		while (!((Parameter) parameters.get(i)).getName().equalsIgnoreCase(
				apn.getName())) {
			System.out.println("parameter name:"
					+ ((Parameter) parameters.get(i)).getName());
			i++;
		}
		Parameter pa = (Parameter) parameters.get(i);

		Type type = pa.getType();
		String typeName = "";
		if (type != null) {
			typeName = type.getName();
		}
		typeName="typeName:"+"typeName";
		String location="x:"+String.valueOf(point.x)+",y:"+String.valueOf(point.y);
		createComment(apNode,typeName+location);
		
		EList<ActivityEdge> inEdges=apn.getIncomings();
		for(ActivityEdge edge:inEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,apNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,apNode);
			}
		}
		EList<ActivityEdge> outEdges=apn.getOutgoings();
		for(ActivityEdge edge:outEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,apNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,apNode);
			}
		}
		
		if(!GraphicProfileHelper.getOrientation(apn).equals(null)){
			Orientation ori=GraphicProfileHelper.getOrientation(apn);
			if(ori==Orientation.EAST){
				createComment(apNode, "orientation:east");
			} else if(ori==Orientation.WEST){
				createComment(apNode,  "orientation:west");
			} else if(ori==Orientation.NORTH){
				createComment(apNode, "orientation:north");
			} else {
				createComment(apNode, "orientation:south");
			}
		}
	}

	public static void createForkNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		ForkNode fork=(ForkNode)ele;
		Point point=GraphicProfileHelper.getLocation(fork);
		Node forkNode=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getForkNode(),fork.getName(),point.x,point.y,layoutSystem, 1);
		EList<ActivityEdge> inEdges=fork.getIncomings();
		for(ActivityEdge edge:inEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,forkNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(), flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,forkNode);
			}
		}
		EList<ActivityEdge> outEdges=fork.getOutgoings();
		for(ActivityEdge edge:outEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(), flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,forkNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(), flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,forkNode);
			}
		}
	}

	public static void createJoinNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		JoinNode join=(JoinNode)ele;
		Point point=GraphicProfileHelper.getLocation(join);
		Node joinNode=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getJoinNode(),join.getName(),point.x,point.y,layoutSystem, 1);
		EList<ActivityEdge> inEdges=join.getIncomings();
		for(ActivityEdge edge:inEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(), flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,joinNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(), flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,joinNode);
			}
		}
		EList<ActivityEdge> outEdges=join.getOutgoings();
		for(ActivityEdge edge:outEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(), flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,joinNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(), flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,joinNode);
			}
		}
		
	}

	public static void createCallBehaviorActionNode(Graph graph,Graph esm,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		CallBehaviorAction cba = (CallBehaviorAction) ele;
		
//		EList<Element> callbehaviorParameters=cba.getOwnedElements();
		Activity behavior = (Activity) ((cba.getBehavior() instanceof Collaboration) ? cba
				.getBehavior().getClassifierBehavior() : cba.getBehavior());

		
//		create esm for the building blcok
		EList<Element> activityBehavior=behavior.getOwnedElements();
			for (Element statemachine : activityBehavior) {
				if ((statemachine instanceof StateMachine)) {
					NamedElement sm=(NamedElement)statemachine;
					createStateMachineNode(esm, sm,layoutSystem);
					break;
				}
			}
		Point point=GraphicProfileHelper.getLocation(cba);
		Node behaviorInstance = createNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getCallBehaviorAction(),cba.getName(),point.x,point.y,layoutSystem,0);
		Dimension dimension=GraphicProfileHelper.getSizeIfDefined(cba);
		String height="height:";
		String width="width:";
		if(dimension!=null){
			height=height+String.valueOf(dimension.height);
			width=width+String.valueOf(dimension.width);
		}
		Element resource = null;
		if (((cba.getBehavior() instanceof Activity))
				&& ((cba.getBehavior().getOwner() instanceof Collaboration)))
			resource = cba.getBehavior().getOwner();
		else if (((cba.getBehavior() instanceof Activity))
				&& (!(cba.getBehavior().getOwner() instanceof Collaboration))) {
			resource = cba.getBehavior();
		}

		String sourceBehavior="behavior:"+resource.eResource().getURI().toString();
				
		System.out.println("cba resource:"
				+ resource.eResource().getURI().toString());

		EList<ActivityPartition> partitions = cba.getInPartitions();
		String partitionNames = "";
		if (partitions.size() > 1)
			for (Object o : partitions.toArray()) {
				ActivityPartition ap = (ActivityPartition) o;
				if (partitionNames == "")
					partitionNames = ap.getName();
				else
					partitionNames = partitionNames + ";" + ap.getName();
			}
		else {
			partitionNames = ((ActivityPartition) partitions.get(0)).getName();
		}
		String inPartitions="inPartition:"+partitionNames;
		String location="hight:"+point.y+",width:"+point.x;

		createComment(behaviorInstance,sourceBehavior+","+inPartitions+","+location+","+height+","+width);
		
		EList<Element> apns = behavior.getOwnedElements();
			for (Element o : apns) {
				if ((o instanceof ActivityParameterNode)) {
					ActivityParameterNode apn = (ActivityParameterNode) o;
					Parameter p = apn.getParameter();
					EList<ParameterSet> ps = p.getParameterSets();
//					Object name;
					if (p.getDirection().equals(
							ParameterDirectionKind.IN_LITERAL)) {
						InputPin inp = null;
						EList<InputPin> ips = cba.getInputs();
						for (InputPin op : ips) {
							if (op.getName().equals(apn.getName()))
								inp = op;
						}
						Node pin = createNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getInputPin(),inp.getName());
						int x=GraphicProfileHelper.getLocation(cba).x;
						int y=GraphicProfileHelper.getLocation(cba).y;
						EList<ActivityEdge> inEdges=inp.getIncomings();
						for(ActivityEdge edge:inEdges){
							if(edge instanceof ControlFlow){
								Node controFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),
										point.x-node_width+x,point.y-node_height+y,layoutSystem,0,flowMap);
								createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,pin);
							}else if (edge instanceof ObjectFlow){
								Node oFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
								createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,pin);
							}
						}
						
							String inPartition="inPartition:"+((ActivityPartition) inp.getInPartitions().get(
									0)).getName();

							String owner="owner:"+cba.getName();
							String typeName ="typeName:";
							try {
								if(!inp.getType().equals(null))
									 typeName = typeName+ inp.getType().getName();
							} catch (Exception e) {
								System.err.println("fail to catch the type of the parameter when create a parameter node:"+inp.getName()+" in "+((NamedElement)inp.getOwner()).getName()+
								"probably because the pin node is a control flow node");
							}
							String pinType="pinType:";
							if (p.isStream())
								pinType=pinType+"INPUT_STREAM";
							else if (ps.size() > 0)
								pinType=pinType+"INPUT_MULTIPLE";
							else {
								pinType=pinType+"INPUT";
							}
							Point iPoint=GraphicProfileHelper.getLocation(apn);
							String ilocation="x:"+iPoint.x+",y:"+iPoint.y;
							createComment(pin,inPartition+","+owner+","+pinType+","+typeName+","+ilocation);
						createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getElement_OwnedElement(),behaviorInstance,pin);
					} else if (p.getDirection().equals(
							ParameterDirectionKind.OUT_LITERAL)) {
						OutputPin outp = null;
						EList<OutputPin>  outps = cba.getResults();
						System.out.print(apn.getName());
						for (OutputPin op1:outps){
							OutputPin op = (OutputPin) op1;
							System.out.print("-----" + op.getName());
							if (op.getName().equals(apn.getName()))
								outp = op;
						}
						System.out.println();
						int x=GraphicProfileHelper.getLocation(outp).x;
						int y=GraphicProfileHelper.getLocation(outp).y;
						Node pin = createNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getOutputPin(),outp.getName(),point.x-node_width+x,point.y-node_height+y,layoutSystem,0);
						
						EList<ActivityEdge> outEdges=outp.getOutgoings();
						for(ActivityEdge edge:outEdges){
							if(edge instanceof ControlFlow){
								Node controFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
								createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,pin);
							}else if (edge instanceof ObjectFlow){
								Node oFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
								createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,pin);
							}
						}
						
						String inPartition="inPartition:"+((ActivityPartition) outp.getInPartitions().get(0)).getName();
						String typeName ="typeName:";
						try {
							if(!outp.getType().equals(null))
								 typeName = typeName+ outp.getType().getName();
						} catch (Exception e) {
							System.err.println("fail to catch the type of the parameter when create a parameter node:"+outp.getName()+" in "+((NamedElement)outp.getOwner()).getName()+
									"probably because the pin node is a control flow node");
						}
						String owner="owner:"+cba.getName();
						String pinType="pinType:";
						if (p.isStream())
							pinType=pinType+"OUTPUT_STREAM";
						else if (ps == null)
							pinType=pinType+"OUTPUT";
						else {
							pinType=pinType+"OUTPUT_MULTIPLE";
						}
						String oLocation="x:"+String.valueOf(x)+",y:"+String.valueOf(y);
						createComment(pin,inPartition+","+typeName+","+owner+","+pinType+","+oLocation);
						createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getElement_OwnedElement(),behaviorInstance,pin);
					}
				}
			}
	}

	private static Node createOrFindFlowNode(Graph graph, EClass controlFlow,
			String name, int i, int j, LayoutSystem layoutSystem, int k,
			HashMap<String, Node> flowMap) {
		if(flowMap.containsKey(name)){
			return flowMap.get(name);
		} else {
			Node node=createNode(graph,controlFlow,name,i,j,layoutSystem,k);
			flowMap.put(name, node);
			return node;
			}		
	}
	public static void createInitialNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		InitialNode initial=(InitialNode)ele;
		Point point=GraphicProfileHelper.getLocation(initial);
		Node initialNode=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getInitialNode(),initial.getName(),point.x,point.y,layoutSystem, 1);
		EList<ActivityEdge> inEdges=initial.getIncomings();
		for(ActivityEdge edge:inEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,initialNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName());
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,initialNode);
			}
		}
		EList<ActivityEdge> outEdges=initial.getOutgoings();
		for(ActivityEdge edge:outEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,initialNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName());
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,initialNode);
			}
		}
	}

	public static void createActivityFinalNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		FinalNode fina=(FinalNode)ele;
		Point point=GraphicProfileHelper.getLocation(fina);
		Node finaNode=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getInitialNode(),fina.getName(),point.x,point.y,layoutSystem, 1);
		EList<ActivityEdge> inEdges=fina.getIncomings();
		for(ActivityEdge edge:inEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,finaNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,finaNode);
			}
		}
		EList<ActivityEdge> outEdges=fina.getOutgoings();
		for(ActivityEdge edge:outEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,finaNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,finaNode);
			}
		}
		
	}

	public static void createFlowFinalNode(Graph buildingBlockGraph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		FlowFinalNode ff=(FlowFinalNode)ele;
		Point point=GraphicProfileHelper.getLocation(ff);
		Node ffNode=createNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getInitialNode(),ff.getName(),point.x,point.y,layoutSystem, 1);
		EList<ActivityEdge> inEdges=ff.getIncomings();
		for(ActivityEdge edge:inEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,ffNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,ffNode);
			}
		}
		EList<ActivityEdge> outEdges=ff.getOutgoings();
		for(ActivityEdge edge:outEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,ffNode);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(buildingBlockGraph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,ffNode);
			}
		}
		
	}

	public static void createReadVariableActionNode(Graph graph,
			NamedElement ele, LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		ReadVariableAction rva = (ReadVariableAction) ele;
		Point location=GraphicProfileHelper.getLocation(rva);
		Node avvan = createNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getAddVariableValueAction(),rva.getName(),location.x,location.y,layoutSystem,0);

		ActivityPartition partition = (ActivityPartition) ((AddVariableValueAction) ele)
				.getInPartitions().toArray()[0];
		String inPartition = "inPartition:"+partition.getName();
		createComment(avvan,inPartition+","+"x:"+location.x+",y:"+location.y);
		
		EList<ActivityEdge> outEdges=rva.getIncomings();
		for(ActivityEdge edge:outEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,avvan);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,avvan);
			}
		}
		
		OutputPin ip = (OutputPin) rva.getOwnedElements().get(0);
			Node valuePin = createNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getInputPin(),rva.getName(),location.x,location.y+node_height,layoutSystem,0);

			String owner="owner:"+rva.getName();
			String typeName="typeName:"+ip.getType().getName();
			String variable="variable:"+ip.getName();
			String variableType="variableType:"+ip.getType().getName();
			
			createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getElement_OwnedElement(),avvan,valuePin);

			EList<ActivityEdge> iEdges=ip.getOutgoings();
			for(ActivityEdge edge:iEdges){
				if(edge instanceof ControlFlow){
					Node controFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
					createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,valuePin);
				}else if (edge instanceof ObjectFlow){
					Node oFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
					createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,valuePin);
				}
			}			
					
	}

	public static void createAddVariableValueActionNode(
			Graph graph, NamedElement ele,
			LayoutSystem layoutSystem,HashMap<String,Node> flowMap) {
		AddVariableValueAction avva = (AddVariableValueAction) ele;
		Point location=GraphicProfileHelper.getLocation(avva);
		Node avvan = createNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getAddVariableValueAction(),avva.getName(),location.x,location.y,layoutSystem,0);



		ActivityPartition partition = (ActivityPartition) ((AddVariableValueAction) ele)
				.getInPartitions().toArray()[0];
		String inPartition = "inPartition:"+partition.getName();
		createComment(avvan,inPartition+","+"x:"+location.x+",y:"+location.y);
		
		EList<ActivityEdge> outEdges=avva.getOutgoings();
		for(ActivityEdge edge:outEdges){
			if(edge instanceof ControlFlow){
				Node controFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
				createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),controFlowNode,avvan);
			}else if (edge instanceof ObjectFlow){
				Node oFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
				createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Source(),oFlowNode,avvan);
			}
		}
		
		InputPin ip = (InputPin) avva.getOwnedElements().get(0);
			Node valuePin = createNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getInputPin(),avva.getName(),location.x,location.y-node_height,layoutSystem,0);

			String owner="owner:"+avva.getName();
			String typeName="typeName:"+ip.getType().getName();
			String variable="variable:"+ip.getName();
			String variableType="variableType:"+ip.getType().getName();
			
			createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getElement_OwnedElement(),avvan,valuePin);

			EList<ActivityEdge> iEdges=ip.getIncomings();
			for(ActivityEdge edge:iEdges){
				if(edge instanceof ControlFlow){
					Node controFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getControlFlow(),edge.getName(),flowMap);
					createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),controFlowNode,valuePin);
				}else if (edge instanceof ObjectFlow){
					Node oFlowNode=createOrFindFlowNode(graph,UMLFactory.eINSTANCE.getUMLPackage().getObjectFlow(),edge.getName(),flowMap);
					createEdge(graph,UMLFactory.eINSTANCE.getUMLPackage().getActivityEdge_Target(),oFlowNode,valuePin);
				}
			}			
			

		
	}
}
