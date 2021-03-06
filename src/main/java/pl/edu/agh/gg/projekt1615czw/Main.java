package pl.edu.agh.gg.projekt1615czw;

import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pl.edu.agh.gg.projekt1615czw.application.production.ProductionNotApplicableException;
import pl.edu.agh.gg.projekt1615czw.application.production.ProductionOne;
import pl.edu.agh.gg.projekt1615czw.application.production.reference.ProductionOneReferenceNodeFinder;
import pl.edu.agh.gg.projekt1615czw.domain.HyperNode;
import pl.edu.agh.gg.projekt1615czw.domain.HyperNodeLabel;
import pl.edu.agh.gg.projekt1615czw.infrastructure.GraphAdapter;

@Slf4j
@PropertySource(value = "classpath:/application.properties", ignoreResourceNotFound = true)
@ComponentScan(basePackages = "pl.edu.agh.gg.projekt1615czw")
@Configuration
public class Main {
    private final ProductionOne productionOne;

    @Autowired
    public Main(ProductionOne productionOne) {
        this.productionOne = productionOne;
    }

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(Main.class).getBean(Main.class).start();
    }

    public void start() {
        // do stuff
        log.info("Application started");

        Graph<HyperNode, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        graph.addVertex(new HyperNode(HyperNodeLabel.S));
        HyperNode referenceNode = new ProductionOneReferenceNodeFinder().findProductionReferenceNode(graph)
                .orElseThrow(ProductionNotApplicableException::new);
        productionOne.applyProduction(graph, referenceNode);
        org.graphstream.graph.Graph graphstreamGraph = new GraphAdapter("Graph 1", graph);
        graphstreamGraph.display();
    }
}
