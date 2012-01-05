Ext.Loader.setConfig({ enabled: true });

Ext.onReady(function(){
    Ext.define('File', {
        extend: 'Ext.data.Model',
        fields: [
            { name: 'id', type: 'string' },
            { name: 'name', type: 'string' },
            { name: 'mimeType', type: 'string' },
            { name: 'encoding', type: 'string' },
            { name: 'lastModified', type: 'string' }
        ],
        idProperty: 'id'
    });

    Ext.define('Folder', {
	    extend: 'Ext.data.Model',
	    fields: [
	        { name: 'id', type: 'string' },
		    { name: 'name', type: 'string' },
		    { name: 'lastModified', type: 'string' }
	    ],
	    idProperty: 'id'
    });

var store = Ext.create('Ext.data.Store', {
	model: 'File',
	proxy: {
		type: 'ajax',
		url: 'files.json',
		extraParams: { 
			node: '00000000-0000-0000-0000-000000000000'
		},
		reader: {
			type: 'json',
			root: 'files'
		}
	},
	autoLoad: true
});

var treeStore = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url : 'folders.json'
    },
    root: {
    	id: '00000000-0000-0000-0000-000000000000',
    	text: 'Repository',
    	expanded: true
    }
});

var grid = Ext.create('Ext.grid.Panel', {
    hideCollapseTool: true,
    region: 'center',
    width: 640,
    store: store,
    columnLines: true,
    columns: [
        {
            text     : 'Name',
            flex     : 1,
            sortable : true,
            dataIndex: 'name'
        },
        {
            text     : 'MIME Type',
            width    : 75,
            sortable : true,
            dataIndex: 'mimeType'
        },
        {
            text     : 'Encoding',
            width    : 75,
            sortable : true,
            dataIndex: 'encoding'
        },
        {
            text     : 'Last Modified',
            width    : 75,
            sortable : true,
            dataIndex: 'lastModified'
        }
    ],
    listeners: {
    	itemClick: function(panel, file) {
    		var target = {
    				href: 'files/' + file.id,
    				title: file.name
    		};
    		Ext.ux.Lightbox.open(target);
    	}
    },
    viewConfig: {
        stripeRows: true
    }
});

var tree = Ext.create('Ext.tree.Panel', {
    store: treeStore,
    hideHeaders: true,
    rootVisible: true,
    width: 320,
    title: 'Folders',
    region: 'west',
    collapsible: false,
    listeners: {
		itemclick: function(view, folder) {
			store.load({ params: { node: folder.getId() }});
    	}
    }
});

var container = Ext.create('Ext.Panel', {
    split:true,
    margins:'5 0 5 5',
    width: 960,
    layout:'border',
       items: [tree, grid]
    });

	Ext.application({
    	name: 'Leabharlann',
    	launch: function() {
        	Ext.create('Ext.container.Viewport', {
            	layout: 'fit',
            	items: [
                    container
                ]
        	});
    	}
	});
});