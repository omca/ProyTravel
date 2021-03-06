package pe.com.paxtravel.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import pe.com.paxtravel.bean.AnimalBean;
import pe.com.paxtravel.bean.CotizacionBean;
import pe.com.paxtravel.bean.CotizacionDetalleBean;
import pe.com.paxtravel.bean.ExpedienteLogBean;
import pe.com.paxtravel.bean.ReservaBean;
import pe.com.paxtravel.bean.ReservaPasajeroDetalleBean;
import pe.com.paxtravel.service.CotizacionService;
import pe.com.paxtravel.service.ReservaService;
import pe.com.paxtravel.util.ControllerUtil;
import pe.com.paxtravel.util.DataJsonBean;
import pe.com.paxtravel.util.Utils;
import pe.gob.sunat.framework.spring.util.conversion.SojoUtil;
//import pe.gob.sunat.framework.uti
//import org.apache.commons.beanutils.BeanUtils;
//import pe.com.paxtravel.service.ProduccionService;

@Controller
public class ReservaController {

	@Autowired
	private ReservaService reservaService;
	
	@Autowired
	private CotizacionService cotizacionService;
	//	
//	@Autowired
//	private CotizacionService cotizacionService;
//	
//	@Autowired
//	private OrdenPlanificacionService ordenPlanificacionService;
		
	private String jsonView;

	public String getJsonView() {
		return jsonView;
	}

	public void setJsonView(String jsonView) {
		this.jsonView = jsonView;
	}
	
	@RequestMapping( value = "/listarReserva", method ={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView listarPaqueteTuristico(HttpServletRequest request, HttpServletResponse response){
	
		ModelAndView modelAndView = null;
		HashMap<String, Object> mapa = new HashMap<String, Object>();

		List<ReservaBean> listarReserva = new ArrayList<ReservaBean>();
		ReservaBean reservaBean = new ReservaBean();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		boolean flag = false;
		DataJsonBean dataJSON = new DataJsonBean();

		try {
			modelAndView = new ModelAndView();
			
			String botonBuscar = (request.getParameter("btnBuscar"))!=null?request.getParameter("btnBuscar"):"";
			
			mapa.put("titulo", "RESERVA");
			
			if("1".equals(botonBuscar)){
				Map<String, Object> parametrosRequest = ControllerUtil.parseRequestToMap(request);
				Map<String, Object> paqueteBeanMap = (Map<String, Object>) parametrosRequest.get("reservaBean");
//				// inserta en el bean todos los valores del mapa (property vs keys)
				BeanUtils.populate(reservaBean, paqueteBeanMap);
				
				
				if (!"".equals(reservaBean.getFechaInicio() ) ) {
					String fechaini = Utils.stringToStringyyyyMMdd(reservaBean.getFechaInicio());
					reservaBean.setFechaInicio(fechaini);
				}
				
				if (!"".equals(reservaBean.getFechaFin())) {
					String fechafin = Utils.stringToStringyyyyMMdd(reservaBean.getFechaFin());
					reservaBean.setFechaFin(fechafin);
				}
				
//				//System.out.print("Numero Orden : " + paqueteTuristicoBean.getIdOrden());
//				System.out.println("Cliente : " + paqueteTuristicoBean.getCliente());
//				System.out.println("Tipo Busqueda : " + paqueteTuristicoBean.getTipoBusqueda());
				
				
				if(reservaBean.getTipoBusqueda() == 1)
					reservaBean.setNumeroDocumento(reservaBean.getCliente());
				else if(reservaBean.getTipoBusqueda() == 2)
					reservaBean.setCliente("%" + reservaBean.getCliente() + "%");
				
				listarReserva = reservaService.listarReserva(reservaBean);
				
				System.out.println("size:... " + listarReserva.size());
				
				
				mapa.put("listaReserva",  listarReserva);
				
				dataJSON.setRespuesta("ok", null, mapa);
				flag = true; 
				
			} else {
				
				listarReserva = reservaService.listarReserva(reservaBean);
//				
				modelAndView.addObject("listaReserva", SojoUtil.toJson(listarReserva) );
//				modelAndView.addObject("mapaDatos", mapa);
				modelAndView.setViewName("reserva/listarReserva");
				
				
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (flag) {
			return ControllerUtil.handleJSONResponse(dataJSON, response);
		} else {
			return modelAndView;
		}
	}
	

	@RequestMapping( value = "/cargarFormRegistrarReserva", method ={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView cargarFormRegistrarCotizacion(HttpServletRequest request, HttpServletResponse response){

		ModelAndView modelAndView = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Map<String, Object> mapaDatos = new HashMap<String, Object>();
		List<AnimalBean> listaToro = new ArrayList<AnimalBean>();
		DataJsonBean dataJSON = new DataJsonBean();

		try {
			
			modelAndView = new ModelAndView();
//			List<CiudadBean> listaCiudad = new ArrayList<CiudadBean>();
//			List<PaisBean> listaPais = new ArrayList<PaisBean>();
//
//			CiudadBean ciudadBean = new CiudadBean();
//			PaisBean paisBean = new PaisBean();
//			ciudadBean.setIdPais(1);
//			listaCiudad = cotizacionService.listarCiudad(ciudadBean);
//			listaPais = cotizacionService.listarPais(paisBean);

			/* listaToro = animalService.listarToro();

			Map<String, Object> mapaListaToro = new HashMap<String, Object>();
			for (AnimalBean animalBean : listaToro) {
				mapaListaToro.put("codigo", animalBean.getCodigoAnimal());
				mapaListaToro.put("descripcion", animalBean.getNombreAnimal());
			}			
			mapa.put("codigoAnimal",(String) request.getParameter("codigoAnimal"));
			mapa.put("nombreAnimal",(String) request.getParameter("nombreAnimal"));
			mapa.put("listaToro", SojoUtil.toJson(mapaListaToro) );
			mapa.put("fechaActual", sdf.format( new Date() ));
			dataJSON.setRespuesta("ok", null, mapa);
			Map<String, Object> mapaDatos = new HashMap<String, Object>();
			mapaDatos.put("listTipoUsuario", listaTipoUsuario); */
			
			mapaDatos.put("titulo", "Registrar Cotizaci&oacute;n");			
			Map<String, Object> mapaListaCiudad = new HashMap<String, Object>();
			
//			for (CiudadBean ciudadBean1 : listaCiudad) {
//				mapaListaCiudad.put("idCiudad", ciudadBean1.getIdCiudad());
//				mapaListaCiudad.put("nomCiudad", ciudadBean1.getNomCiudad());
//			}

//			mapaDatos.put("listCiudad", listaCiudad);
//			mapaDatos.put("listPais", listaPais);

//			modelAndView.addObject("numeroCotizacion", cotizacionService.generarNumeroCotizacion()+"");
			modelAndView.addObject("titulo", "REGISTRAR RESERVA");
			modelAndView.addObject("mapaDatos", mapaDatos);
			modelAndView.addObject("fechaReserva", Utils.dateUtilToStringDDMMYYYY( new Date() )) ;
			modelAndView.setViewName("reserva/registrarReserva");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return modelAndView;
	}
	
	
	@RequestMapping( value = "/obtenerCotizacion", method ={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView obtenerCotizacion(HttpServletRequest request, HttpServletResponse response){

		Map<String, Object> mapa = new HashMap<String, Object>();
		DataJsonBean dataJSON = new DataJsonBean();
		try {

			List<ReservaBean> obtenerCotizacion = new ArrayList<ReservaBean>();
			List<ReservaBean> listaTicketAereo = new ArrayList<ReservaBean>();
			List<ReservaBean> listaPaqueteTuristico = new ArrayList<ReservaBean>();
			
			ReservaBean reservaBean = new ReservaBean();
			reservaBean.setNumeroCotizacion(request.getParameter("numeroCotizacion"));
			
			obtenerCotizacion = reservaService.obtenerCotizacion(reservaBean);

			mapa.put("titulo", "Cliente");
			String idTipoCotizacion = "";
	        if (obtenerCotizacion.size() > 0){
	        	idTipoCotizacion = obtenerCotizacion.get(0).getIdTipoCotizacion() + "";
	        	
		        mapa.put("nombreCliente", obtenerCotizacion.get(0).getCliente());
		        mapa.put("documentoCliente", obtenerCotizacion.get(0).getNumeroDocumento());
		        mapa.put("direccionCliente", obtenerCotizacion.get(0).getDireccion());
		        mapa.put("telefonoCliente", obtenerCotizacion.get(0).getTelefonoCliente());
		        mapa.put("fechaCotizacion", obtenerCotizacion.get(0).getFechaCotizacion());
		        mapa.put("tipoCotizacion", obtenerCotizacion.get(0).getNombreTipoCotizacion());
		        mapa.put("estadoCotizacion", obtenerCotizacion.get(0).getEstadoCotizacion());
		        mapa.put("numeroAdultos", obtenerCotizacion.get(0).getNumeroAdultos());
		        mapa.put("numeroNinos", obtenerCotizacion.get(0).getNumeroNinos());
		        mapa.put("precioCotizacion", obtenerCotizacion.get(0).getPrecioCotizacion());
		        mapa.put("idCotizacion", obtenerCotizacion.get(0).getIdCotizacion());
		        mapa.put("idCliente", obtenerCotizacion.get(0).getIdCliente());
		        mapa.put("idTipoCotizacion", obtenerCotizacion.get(0).getIdTipoCotizacion());
		        mapa.put("status", "1");
		       
	        } else {
	        	mapa.put("status", "0");
	        	mapa.put("nombreCliente", "");
		        mapa.put("idCliente", "" );
		        mapa.put("mensajeCliente", "No se encontro el cliente. ");

	        }

	        listaTicketAereo = reservaService.obtenerTicketAereoCotizacion(reservaBean);
	        listaPaqueteTuristico = reservaService.listarPaqueteCotizacion(reservaBean);
	        
			mapa.put("listaTicketAereo", listaTicketAereo);
			mapa.put("listaPaqueteTuristico", listaPaqueteTuristico);

	        dataJSON.setRespuesta("ok", null, mapa);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return ControllerUtil.handleJSONResponse(dataJSON, response);
	}
	
	
	@RequestMapping( value = "/grabarReserva", method ={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView grabarReserva(HttpServletRequest request, HttpServletResponse response){
		
		System.out.println("grabar paquete ...........................................");
		
		Map<String, Object> mapa = new HashMap<String, Object>();
		DataJsonBean dataJSON = new DataJsonBean();		
		
		try {
			
			mapa.put("titulo", "Grabar Paquete");			
			
			String datosPasajeros = request.getParameter("datosPasajeros");
			String idCotizacion = request.getParameter("idCotizacion");
			ReservaBean reservaBean = new ReservaBean();
			
			Map<String, Object> parametrosRequest = ControllerUtil.parseRequestToMap(request);
			Map<String, Object> reservaBeanMap = (Map<String, Object>) parametrosRequest.get("reservaBean");
			BeanUtils.populate(reservaBean, reservaBeanMap);
			
			String numeroReserva = reservaService.generarNumeroReserva();
			reservaBean.setNumeroReserva(numeroReserva);
			reservaBean.setIdCotizacion( Integer.parseInt( idCotizacion ) );
			reservaBean.setIdCliente( Integer.parseInt( request.getParameter("idCliente") ) );
			reservaBean.setIdTipoCotizacion( Integer.parseInt( request.getParameter("idTipoCotizacion") ) );
			reservaBean.setFechaReserva( Utils.dateUtilToStringYYYYMMDD( new Date() ) );
			reservaBean.setFechaRegistro( Utils.dateUtilToStringYYYYMMDD( new Date() ) );
			reservaBean.setIdUsuario(0);
			reservaBean.setEstadoCotizacion("12");
			
			int idReserva = reservaService.registrarReserva(reservaBean);
			
			// registrando a los pasajeros de la reserva
			if ( datosPasajeros.length() > 0 ) {

				String pasajeros[] = datosPasajeros.split(",");				
				String camposPasajeros[];
				if ( pasajeros.length > 0 ) {		
					
					System.out.println("cantidad de vuelos" + pasajeros.length);
				
					for (int i = 0; i < pasajeros.length; i++) {
						
						System.out.println("sString de detalle de pasajeros["+i+"]" + pasajeros[i]);
	
						camposPasajeros = pasajeros[i].split("_");
						
						ReservaPasajeroDetalleBean pasajeroDeta = new ReservaPasajeroDetalleBean();
						
						pasajeroDeta.setIdReserva(idReserva);
						pasajeroDeta.setTipoDocumento( camposPasajeros[0] );
						pasajeroDeta.setNumeroDocumento( camposPasajeros[1] );
						pasajeroDeta.setNombres( camposPasajeros[2] );
						pasajeroDeta.setApellidos( camposPasajeros[3] );
						pasajeroDeta.setFechaNacimiento( camposPasajeros[4] );
						pasajeroDeta.setIdParentesco( Integer.parseInt(camposPasajeros[5]) );
						
						int res = reservaService.registrarReservaPasajero(pasajeroDeta);
					}
				}												
			}
			
			// actualiza la cotización a estado Reservado - 11
			CotizacionBean cotizacionBean = new CotizacionBean();
			cotizacionBean.setIdEstado(11);
			cotizacionBean.setIdCotizacion( Integer.parseInt( idCotizacion ) );
			cotizacionService.actualizarCotizacion(cotizacionBean);
			
			// expediente - auditoria
			// registra en auditoria el cambio de estado en COTIZACION - Reservado
			ExpedienteLogBean expedienteLogBean = new ExpedienteLogBean();
			expedienteLogBean.setTiLog("COTIZA");
			expedienteLogBean.setIdTx( Integer.parseInt( idCotizacion ) );
			expedienteLogBean.setIdUser(0);
			expedienteLogBean.setIdEstado(11);
			expedienteLogBean.setDesLog("Cotizacion - Estado Reservado");
			cotizacionService.registrarExpedienteLog(expedienteLogBean);
			
			// registra en auditoria el cambio de estado en RESERVA - Confirmado
			expedienteLogBean.setTiLog("RESERVA");
			expedienteLogBean.setIdTx( idReserva );
			expedienteLogBean.setIdUser(0);
			expedienteLogBean.setIdEstado(12);
			expedienteLogBean.setDesLog("Reserva - Estado Confirmado");
			cotizacionService.registrarExpedienteLog(expedienteLogBean);

			mapa.put("numeroReserva", numeroReserva);
			
			dataJSON.setRespuesta("ok", null, mapa);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return ControllerUtil.handleJSONResponse(dataJSON, response);
	}
	
	@RequestMapping( value = "/verDetalleReserva", method ={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView verDetalleReserva(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> mapa = new HashMap<String, Object>();
		DataJsonBean dataJSON = new DataJsonBean();
		try {
			ReservaBean o = new ReservaBean();
			int idReserva = Integer.parseInt( request.getParameter("idReserva") );
			o.setIdReserva(idReserva);
			o = reservaService.obtenerReserva(o);
	        mapa.put("titulo", "Detalle Reserva");
	        mapa.put("reserva", o);
	        dataJSON.setRespuesta("ok", null, mapa);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return ControllerUtil.handleJSONResponse(dataJSON, response);
	}
	
	
	@RequestMapping( value = "/enviarReservaCliente", method ={RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView enviarReservaCliente(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> mapa = new HashMap<String, Object>();
		DataJsonBean dataJSON = new DataJsonBean();
		try {
			
			int idReserva = Integer.parseInt( request.getParameter("idReserva") );
			String numReserva = request.getParameter("numReserva");
			
			// actualiza la reserva a estado Emitido - 13
			ReservaBean reservaBean = new ReservaBean();
			reservaBean.setIdReserva( idReserva );
			reservaBean.setIdEstadoReserva("13");
			reservaService.actualizarEstadoReserva(reservaBean);
			
			ExpedienteLogBean expedienteLogBean = new ExpedienteLogBean();
			 
			// registra en auditoria el cambio de estado en RESERVA - Emitido
			expedienteLogBean.setTiLog("RESERVA");
			expedienteLogBean.setIdTx( idReserva );
			expedienteLogBean.setIdUser(0);
			expedienteLogBean.setIdEstado(13);
			expedienteLogBean.setDesLog("Reserva - Estado Emitido");
			cotizacionService.registrarExpedienteLog(expedienteLogBean);
			
			
	        mapa.put("titulo", "Detalle Reserva");
	        mapa.put("numReserva", numReserva);
	        dataJSON.setRespuesta("ok", null, mapa);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return ControllerUtil.handleJSONResponse(dataJSON, response);
	}
	
	
}
