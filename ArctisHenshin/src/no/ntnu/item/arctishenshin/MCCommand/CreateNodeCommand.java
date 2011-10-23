package no.ntnu.item.arctishenshin.MCCommand;

import org.eclipse.emf.henshin.model.Graph;
import org.eclipse.emf.henshin.model.Node;
import org.eclipse.emf.ecore.EClass;

import de.tub.tfs.henshin.editor.layout.HenshinLayoutFactory;
import de.tub.tfs.henshin.editor.layout.LayoutSystem;
import de.tub.tfs.henshin.editor.layout.NodeLayout;

public class CreateNodeCommand {
	  public static final int X_DEFAULT = 30;
	  public static final int Y_DEFAULT = 30;
	  private Graph graph;
	  private Node node;
	  private NodeLayout nodeLayout;
	  private EClass type;
	  private LayoutSystem layoutSystem;
	  private int x;
	  private int y;
	  private boolean enabled;
	  private boolean multi;
	public CreateNodeCommand(Graph graph, Node node, int x, int y, EClass eClass) {
		this.node=node;
		this.x=x;
		this.y=y;
		type=eClass;
		this.graph=graph;
	}
	public NodeLayout getNodeLayout() {
		if (this.nodeLayout == null) {
			this.nodeLayout = HenshinLayoutFactory.eINSTANCE.createNodeLayout();
		}
		this.nodeLayout.setNode(this.node);
		return this.nodeLayout;
	}
}
