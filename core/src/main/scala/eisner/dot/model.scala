package eisner
package dot

final case class DiGraph(subgraphs: List[SubGraph], edges: Vector[Edge], topics: Set[Topic], stores: Set[Store])
final object DiGraph {
  final val empty: DiGraph = DiGraph(Nil, Vector.empty, Set.empty, Set.empty)

  implicit final val diGraphWriter: Writer[DiGraph] =
    Writer.instance { case (DiGraph(sgs, es, ts, ss), _) =>
      "digraph G {" ::
        s"""${1.tabs}label = "Kafka Streams Topology"""" ::
        sgs.reverse.write(1) :::
        es.write(1) :::
        ts.write(1) :::
        ss.write(1) :::
        "}" ::
        Nil
    }
}

final case class SubGraph(id: String, label: String, edges: Vector[Edge], color: String)
final object SubGraph {
  final def empty(id: String, label: String, color: String): SubGraph = SubGraph(id, label, Vector.empty, color)

  implicit final val subgraphWriter: Writer[SubGraph] =
    Writer.instance { case (SubGraph(id, l, es, c), i) =>
      s"${i.tabs}subgraph cluster_$id {" ::
        s"""${(i + 1).tabs}label = "$l";""" ::
        s"${(i + 1).tabs}style = filled;" ::
        s"${(i + 1).tabs}color = $c;" ::
        s"${(i + 1).tabs}node [style = filled, color = white];" ::
        es.write(i + 1) :::
        s"${i.tabs}}" ::
        Nil
    }
}

final case class Edge(from: String, to: String)
final object Edge {
  implicit final val edgeWriter: Writer[Edge] =
    Writer.instance { case (Edge(f, t), i) => s"""${i.tabs}"$f" -> "$t";""" :: Nil }
}

final case class Topic(name: String, color: String)
final object Topic {
  implicit final val topicWriter: Writer[Topic] =
    Writer.instance { case (Topic(t, c), i) =>
      s"""${i.tabs}"$t" [shape = rect; color = $c];""" :: Nil
    }
}

final case class Store(name: String, color: String)
final object Store {
  implicit final val storeWriter: Writer[Store] =
    Writer.instance { case (Store(s, c), i) =>
      s"""${i.tabs}"$s" [shape = cylinder; color = $c];""" :: Nil
    }
}
