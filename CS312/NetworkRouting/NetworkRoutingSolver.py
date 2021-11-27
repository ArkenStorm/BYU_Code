#!/usr/bin/python3


from CS312Graph import *
import time


class NetworkRoutingSolver:
    def __init__( self ):
        pass

    def initializeNetwork( self, network ):
        assert( type(network) == CS312Graph )
        self.network = network

    def getShortestPath( self, destIndex ):
        self.dest = destIndex
        path_edges = []
        total_length = 0
        src_node = self.network.nodes[self.source]
        dest_node = self.network.nodes[self.dest]
        prev_node = dest_node
        while prev_node.prev is not None:
            edge = prev_node.prev
            path_edges.append( (edge.src.loc, edge.dest.loc, '{:.0f}'.format(edge.length)) )
            total_length += edge.length
            prev_node = edge.src
        if prev_node != src_node:
            return {'cost':float("inf"), 'path':[]}

        return {'cost':total_length, 'path':path_edges}

    def computeShortestPaths( self, srcIndex, use_heap=False ):
        self.source = srcIndex
        t1 = time.time()

        if (use_heap):
            self.heap_dijkstra(srcIndex)
        else:
            self.array_dijkstra(srcIndex)

        t2 = time.time()
        return (t2-t1)

    def heap_dijkstra(self, srcIndex):
        node_indices = {}
        queue = []
        self.network.nodes[srcIndex].dist = 0
        self.make_heap_queue(queue, node_indices)
        while len(queue) > 0:
            node = self.heap_delete_min(queue, node_indices)
            for edge in node.neighbors:
                if edge.dest.dist > edge.src.dist + edge.length:
                    edge.dest.dist = edge.src.dist + edge.length
                    edge.dest.prev = edge
                    self.decrease_key(queue, node_indices, edge.dest)

    def array_dijkstra(self, srcIndex):
        queue = self.make_array_queue()
        queue[srcIndex].dist = 0
        while len(queue) > 0:
            node = self.array_delete_min(queue)
            for edge in node.neighbors:
                if edge.dest.dist > edge.src.dist + edge.length:
                    edge.dest.dist = edge.src.dist + edge.length
                    edge.dest.prev = edge

    def make_heap_queue(self, queue, node_indices):
        for node in self.network.nodes:
            queue.append(node)
            self.bubble_up(queue, len(queue) - 1, node_indices)

    def make_array_queue(self):
        return self.network.nodes.copy()

    def heap_delete_min(self, queue, node_indices):
        if len(queue) > 1:
            min_node = queue[0]
            queue[0] = queue.pop()
        else:
            return queue.pop()
        self.sift_down(queue, 0, node_indices)
        return min_node

    def array_delete_min(self, queue):
        min_node = None
        min_index = -1
        for i in range(len(queue)):
            if min_node is None or min_node.dist > queue[i].dist:
                min_node = queue[i]
                min_index = i
        queue.pop(min_index)
        return min_node

    def decrease_key(self, queue, node_indices, node):
        self.bubble_up(queue, node_indices[node], node_indices)

    def bubble_up(self, queue, index, node_indices):
        parent_index = (index - 1) // 2
        node_indices[queue[index]] = index
        while index != 0 and queue[index].dist < queue[parent_index].dist:
            node_indices[queue[index]], node_indices[queue[parent_index]] = (parent_index, index)
            queue[index], queue[parent_index] = (queue[parent_index], queue[index])
            index = parent_index
            parent_index = (index - 1) // 2

    def sift_down(self, queue, index, node_indices):
        min_child_index = self.min_child(queue, index)
        node_indices[queue[index]] = index
        while min_child_index != 0 and queue[index].dist > queue[min_child_index].dist:
            node_indices[queue[index]], node_indices[queue[min_child_index]] = (min_child_index, index)
            queue[index], queue[min_child_index] = (queue[min_child_index], queue[index])
            index = min_child_index
            min_child_index = self.min_child(queue, index)

    def min_child(self, queue, i):
        if (i + 1) * 2 > len(queue):
            return 0
        elif (i + 1) * 2 == len(queue):
            return ((i + 1) * 2) - 1
        else:
            a = ((i + 1) * 2) - 1
            b = (i + 1) * 2
            return a if queue[a].dist < queue[b].dist else b
