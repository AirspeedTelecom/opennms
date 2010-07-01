package org.opennms.features.poller.remote.gwt.client;

import java.util.HashMap;
import java.util.Map;

import org.opennms.features.poller.remote.gwt.client.events.GWTMarkerClickedEvent;
import org.opennms.features.poller.remote.gwt.client.events.MapPanelBoundsChangedEvent;
import org.opennms.features.poller.remote.gwt.client.utils.BoundsBuilder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwtmapquest.transaction.MQAIcon;
import com.googlecode.gwtmapquest.transaction.MQALargeZoomControl;
import com.googlecode.gwtmapquest.transaction.MQALatLng;
import com.googlecode.gwtmapquest.transaction.MQAPoi;
import com.googlecode.gwtmapquest.transaction.MQAPoint;
import com.googlecode.gwtmapquest.transaction.MQARectLL;
import com.googlecode.gwtmapquest.transaction.MQAShapeCollection;
import com.googlecode.gwtmapquest.transaction.MQATileMap;
import com.googlecode.gwtmapquest.transaction.event.DblClickEvent;
import com.googlecode.gwtmapquest.transaction.event.DblClickHandler;
import com.googlecode.gwtmapquest.transaction.event.MoveEndEvent;
import com.googlecode.gwtmapquest.transaction.event.MoveEndHandler;
import com.googlecode.gwtmapquest.transaction.event.ZoomEndEvent;
import com.googlecode.gwtmapquest.transaction.event.ZoomEndHandler;

/**
 * <p>MapQuestMapPanel class.</p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public class MapQuestMapPanel extends Composite implements MapPanel, HasDoubleClickHandlers, HasClickHandlers {

    private class DefaultMarkerClickHandler implements ClickHandler {

        private GWTMarkerState m_markerState;

        public DefaultMarkerClickHandler(final GWTMarkerState markerState) {
            setMarkerState(markerState);
        }

        public void onClick(final ClickEvent event) {
            m_eventBus.fireEvent(new GWTMarkerClickedEvent(getMarkerState()));
        }

        public void setMarkerState(final GWTMarkerState markerState) {
            m_markerState = markerState;
        }

        public GWTMarkerState getMarkerState() {
            return m_markerState;
        }

    }
    
    private class ClickCounter{
        
        private int m_incr = 0;
        private MQALatLng m_latlng = null;
        private Timer m_timer = new Timer() {

            @Override
            public void run() {
                if(m_incr == 1) {
                    m_map.panToLatLng(m_latlng);
                }else if(m_incr == 3) {
                    m_map.setCenter(m_latlng);
                    m_map.zoomIn();
                }
                
                m_incr = 0;
            }
            
        };
        
        public void incrementCounter(MQALatLng latLng) {
            m_incr++;
            m_latlng = latLng;
            m_timer.cancel();
            m_timer.schedule(300);
        }
        
    }

    private static MapQuestMapPanelUiBinder uiBinder = GWT.create(MapQuestMapPanelUiBinder.class);

    @UiField
    SimplePanel m_mapHolder;

    private MQATileMap m_map;

    private Map<String, OnmsPoi> m_markers = new HashMap<String, OnmsPoi>();

    private HandlerManager m_eventBus;
    
    private ClickCounter m_clickCounter = new ClickCounter();

    private Map<String, MQAShapeCollection<OnmsPoi>> m_markerLayers = new HashMap<String, MQAShapeCollection<OnmsPoi>>();

    interface MapQuestMapPanelUiBinder extends UiBinder<Widget, MapQuestMapPanel> {
    }

    /**
     * <p>Constructor for MapQuestMapPanel.</p>
     *
     * @param eventBus a {@link com.google.gwt.event.shared.HandlerManager} object.
     */
    public MapQuestMapPanel(final HandlerManager eventBus) {
        m_eventBus = eventBus;
        initWidget(uiBinder.createAndBindUi(this));
        m_map = MQATileMap.newInstance(getMapHolder().getElement());
        
        initializeMap();
        
        m_map.addMoveEndHandler(new MoveEndHandler() {
            public void onMoveEnd(final MoveEndEvent event) {
                m_eventBus.fireEvent(new MapPanelBoundsChangedEvent(getBounds()));
            }
        });
        
        m_map.addClickHandler(new com.googlecode.gwtmapquest.transaction.event.ClickHandler() {
            
            public void onClicked(final com.googlecode.gwtmapquest.transaction.event.ClickEvent event) {
                m_clickCounter.incrementCounter(event.getLL());
            }
        });
        
        m_map.addDblClickHandler(new DblClickHandler() {
            
            public void onDblClicked(DblClickEvent event) {
                m_clickCounter.incrementCounter(event.getLL());
            }
        });
        
        m_map.addZoomEndHandler(new ZoomEndHandler() {
            public void onZoomEnd(ZoomEndEvent event) {
                m_eventBus.fireEvent(new MapPanelBoundsChangedEvent(getBounds()));
            }
        });
    }
    
    
    /** {@inheritDoc} */
    @Override
    protected void onLoad() {
        super.onLoad();
        syncMapSizeWithParent();
    }

    /**
     * <p>initializeMap</p>
     */
    public void initializeMap() {
        getMapHolder().setSize("100%", "100%");
        m_map.addControl(MQALargeZoomControl.newInstance());
        m_map.setZoomLevel(1);
        m_map.setCenter(MQALatLng.newInstance("0,0"));

        Window.addResizeHandler(new ResizeHandler() {
            public void onResize(ResizeEvent event) {
                syncMapSizeWithParent();
            }
        });
    }

    /** {@inheritDoc} */
    public void showLocationDetails(final String name, final String htmlTitle, final String htmlContent) {
        final OnmsPoi point = getMarker(name);
        if (point != null) {
            final MQALatLng latLng = point.getLatLng();
            m_map.setCenter(latLng);
            m_map.getInfoWindow().hide();

            point.setInfoTitleHTML(htmlTitle);
            point.setInfoContentHTML(htmlContent);
            point.showInfoWindow();
            final NodeList<Element> elements = Document.get().getElementsByTagName("div");
            for (int i = 0; i < elements.getLength(); i++) {
                final Element e = elements.getItem(i);
                if (e.getClassName().equals("mqpoicontenttext")) {
                    final Style s = e.getStyle();
                    s.setOverflow(Overflow.HIDDEN);
                    break;
                }
            }
        }
    }

    private OnmsPoi createMarker(final GWTMarkerState marker) {
        final MQALatLng latLng = toMQALatLng(marker.getLatLng());
        final MQAIcon icon = createIcon(marker);
        final OnmsPoi point = (OnmsPoi)OnmsPoi.newInstance(latLng, icon);
        point.setStatus(marker.getStatus().name());
        point.setIconOffset(MQAPoint.newInstance(-16, -32));
        point.addClickHandler(new DefaultMarkerClickHandler(marker));
        point.setMaxZoomLevel(16);
        point.setMinZoomLevel(1);
        point.setRolloverEnabled(true);

        return point;
    }

    private MQAIcon createIcon(final GWTMarkerState marker) {
        return MQAIcon.newInstance(marker.getImageURL(), 32, 32);
    }

    /**
     * <p>getBounds</p>
     *
     * @return a {@link org.opennms.features.poller.remote.gwt.client.GWTBounds} object.
     */
    public GWTBounds getBounds() {
        return toGWTBounds(m_map.getBounds());
    }

    /** {@inheritDoc} */
    public void setBounds(final GWTBounds b) {
        m_map.zoomToRect(toMQARectLL(b));
    }

    private static MQALatLng toMQALatLng(final GWTLatLng latLng) {
        return MQALatLng.newInstance(latLng.getLatitude(), latLng.getLongitude());
    }

    private static GWTBounds toGWTBounds(final MQARectLL bounds) {
        final BoundsBuilder bldr = new BoundsBuilder();
        bldr.extend(bounds.getUpperLeft().getLatitude(), bounds.getUpperLeft().getLongitude());
        bldr.extend(bounds.getLowerRight().getLatitude(), bounds.getLowerRight().getLongitude());
        return bldr.getBounds();
    }

    private static MQARectLL toMQARectLL(final GWTBounds bounds) {
        final MQALatLng ne = toMQALatLng(bounds.getNorthEastCorner());
        final MQALatLng sw = toMQALatLng(bounds.getSouthWestCorner());
        final MQARectLL mqBounds = MQARectLL.newInstance(ne, sw);
        return mqBounds;
    }

    private SimplePanel getMapHolder() {
        return m_mapHolder;
    }

    private void syncMapSizeWithParent() {
        m_map.setSize();
    }

    public MQAShapeCollection<OnmsPoi> getLayer(final String layerName) {
        if (layerName == null) return null;
        MQAShapeCollection<OnmsPoi> points = m_markerLayers.get(layerName);
        if (points == null) {
            points = MQAShapeCollection.newInstance();
            m_map.addShapeCollection(points);
        }
        return points;
    }

    /** {@inheritDoc} */
    public void placeMarker(final GWTMarkerState marker) {
        OnmsPoi m = getMarker(marker.getName());

        final MQAShapeCollection<OnmsPoi> newLayer = getLayer(marker.getStatus().name());
        if (m == null) {
            m = createMarker(marker);
            m_markers.put(marker.getName(), m);
            newLayer.add(m);
        } else {
            Window.alert("Status = " + m.getStatus());
            final MQAShapeCollection<OnmsPoi> oldLayer = getLayer(m.getStatus());
            oldLayer.removeItem(m);
            newLayer.add(m);
            updateMarker(m, marker);
        }

    }

    private void updateMarker(final OnmsPoi m, final GWTMarkerState marker) {
        m.setIcon(createIcon(marker));
        m.setVisible(marker.isVisible());
        final MQAShapeCollection<OnmsPoi> shapes = m_markerLayers.get(marker.getStatus().name());
        shapes.add(m);
    }

    private OnmsPoi getMarker(final String name) {
        return m_markers.get(name);
    }

    /**
     * <p>getWidget</p>
     *
     * @return a {@link com.google.gwt.user.client.ui.Widget} object.
     */
    public Widget getWidget() {
        return this;
    }

    /** {@inheritDoc} */
    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
        return addDomHandler(handler, DoubleClickEvent.getType());
    }

    /** {@inheritDoc} */
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    private static final class OnmsPoi extends MQAPoi {
        protected OnmsPoi() {
        }

        public final native void setStatus(final String status) /*-{
            this.setValue('status', status);
        }-*/;

        public final native String getStatus() /*-{
            return this.getValue('status');
        }-*/;
    }

}
