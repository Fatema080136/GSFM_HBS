package org.mysimulationmodel.simulation.agent;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.mysimulationmodel.simulation.common.CInputFormat;
import org.mysimulationmodel.simulation.constant.CVariableBuilder;
import org.mysimulationmodel.simulation.environment.CEnvironment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Vector2d;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CPedestrianGenerator extends IBaseAgentGenerator<IBaseRoadUser>
{

    /**
     * for fixed start and goal position
     */
    private CopyOnWriteArrayList<Vector2d> m_positions = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Vector2d> m_goalpositions = new CopyOnWriteArrayList<>();
    private static final int m_pixelpermeter = CEnvironment.getpixelpermeter();
    /* Gaussian distribution of speed
    private static final double m_GaussianMean = 0.67;
    private static final double m_GaussianStandardDeviation = 0.25;
    private Random rand = new Random();
    private static final double m_GaussianMeanMaxSpeed = 1.2;
    private static final double m_GaussianStandardDeviationMaxSpeed = 0.25;
    */
    /**
     * environment
     */
    private final CEnvironment m_environment;
    private final List<CInputFormat> m_inputdata;



    /**
     * constructor of the generator
     * @param p_stream ASL code as any stream e.g. FileInputStream
     * @throws Exception Thrown if something goes wrong while generating agents.
     */
    public CPedestrianGenerator(@Nonnull final InputStream p_stream, final CEnvironment p_environment, final List<CInputFormat> p_inputdata ) throws Exception
    {
        super(
                // input ASL stream
                p_stream,
                // a set with all possible actions for the agent
                Stream.concat(
                        // we use all build-in actions of LightJason
                        CCommon.actionsFromPackage(),
                        // use the actions which are defined inside the agent class
                        CCommon.actionsFromAgentClass( IBaseRoadUser.class )

                        // build the set with a collector
                ).collect( Collectors.toSet() ),
                // variable builder
                new CVariableBuilder()
        );

        m_environment = p_environment;
        m_inputdata = Collections.synchronizedList(p_inputdata);

    }

    /**
     * generator method of the agent
     * @param p_data any data which can be put from outside to the generator method
     * @return returns an agent
     */
    @Override
    public final IBaseRoadUser generatesingle( @Nullable final Object... p_data )
    {
        // create agent with a reference to the environment
        final IBaseRoadUser l_pedestrian = new IBaseRoadUser( m_configuration, m_environment,1.2*m_pixelpermeter );//0.083
        //long l_counter = m_counter.getAndIncrement();
        CInputFormat l_ped = m_inputdata.remove(0);
        // initialize pedestrian's state

        l_pedestrian.setPosition( l_ped.m_startx_axis*m_pixelpermeter, l_ped.m_starty_axis*m_pixelpermeter);
        l_pedestrian.setGoalPedestrian( l_ped.m_endx_axis*m_pixelpermeter, l_ped.m_endy_axis*m_pixelpermeter );

        l_pedestrian.setradius( 0.75*m_pixelpermeter );// 1.25//.25
        l_pedestrian.setLengthradius(0.75*m_pixelpermeter ); //1.25//.25

        l_pedestrian.setname( l_ped.m_roaduser_id );
        l_pedestrian.settype( 1 );
        l_pedestrian.setmaxforce( 2*m_pixelpermeter );//1//0.09

        l_pedestrian.setSpeed( l_ped.m_speed*m_pixelpermeter* m_environment.getTimestep() );
        //l_pedestrian.setMaxSpeed(( rand.nextGaussian() * m_GaussianStandardDeviationMaxSpeed + m_GaussianMeanMaxSpeed ) * m_pixelpermeter * m_environment.getTimestep());

        l_pedestrian.setMaxSpeed( l_ped.m_max_speed*m_pixelpermeter *m_environment.getTimestep() );
        //l_pedestrian.setMaxSpeed(( rand.nextGaussian() * m_GaussianStandardDeviationMaxSpeed + m_GaussianMeanMaxSpeed ) * m_pixelpermeter * m_environment.getTimestep());

        // universal parameter values
        //l_pedestrian.updateParameter(0.25*m_pixelpermeter, 0.91*m_pixelpermeter,6*m_pixelpermeter, 0.35,
          //0.1*m_pixelpermeter, 11.7*m_pixelpermeter,6*m_pixelpermeter,8*m_pixelpermeter);

        if(l_ped.m_pedtype == 0)
        {   // pca
            l_pedestrian.updateParameter(0.29*m_pixelpermeter, 0.3*m_pixelpermeter, 11.0*m_pixelpermeter,
                    0.43, 1.1*m_pixelpermeter, 17.2*m_pixelpermeter,11*m_pixelpermeter, 8*m_pixelpermeter);

            // not pca 7.8
            l_pedestrian.updateParameter(0.17*m_pixelpermeter, 0.1*m_pixelpermeter, 7.6*m_pixelpermeter, 0.35, 0.1*m_pixelpermeter,
                    15.1*m_pixelpermeter,7.6*m_pixelpermeter, 8*m_pixelpermeter);
            // 2 var
            //l_pedestrian.updateParameter(0.25*m_pixelpermeter, 0.91*m_pixelpermeter,6*m_pixelpermeter,
            //     0.1,   0.1*m_pixelpermeter, 11.7*m_pixelpermeter,  6*m_pixelpermeter,8*m_pixelpermeter);

        }
        else if (l_ped.m_pedtype == 1)
        {   //pca
            l_pedestrian.updateParameter(0.17*m_pixelpermeter, 0.8*m_pixelpermeter, 9.0*m_pixelpermeter,
                    0.37, 0.5*m_pixelpermeter, 11.9*m_pixelpermeter,9*m_pixelpermeter, 8*m_pixelpermeter);

            // not pca ,7.8*m_pixelpermeter
            l_pedestrian.updateParameter(0.24000000000000002*m_pixelpermeter, 0.7*m_pixelpermeter, 12.0*m_pixelpermeter, 0.33999999999999997,
                    0.1*m_pixelpermeter, 17.3*m_pixelpermeter,12*m_pixelpermeter, 8*m_pixelpermeter);
            // 2 var
            //l_pedestrian.updateParameter(0.25*m_pixelpermeter, 0.91*m_pixelpermeter,9*m_pixelpermeter, 0.11,
            //0.1*m_pixelpermeter, 11.7*m_pixelpermeter,  9*m_pixelpermeter,8*m_pixelpermeter);

        }
        else
        {   //pca
            //l_pedestrian.updateParameter(0.16*m_pixelpermeter, 0.1*m_pixelpermeter, 3*m_pixelpermeter, 0.8,
            //   0.1*m_pixelpermeter, 19.3*m_pixelpermeter,3*m_pixelpermeter, 8*m_pixelpermeter);

            // not pca
            l_pedestrian.updateParameter(0.25*m_pixelpermeter, 0.2*m_pixelpermeter, 7.8*m_pixelpermeter, 0.42000000000000004,
                    1.9*m_pixelpermeter, 11.9*m_pixelpermeter, 7.8*m_pixelpermeter, 8*m_pixelpermeter);

            // 2 var
            //l_pedestrian.updateParameter(0.25*m_pixelpermeter, 0.91*m_pixelpermeter,6*m_pixelpermeter, 0.28,
            //     0.1*m_pixelpermeter, 11.7*m_pixelpermeter,6*m_pixelpermeter,8*m_pixelpermeter);

        }

        l_pedestrian.setVelocity( l_pedestrian.getSpeed(), l_pedestrian.getGoalposition(), l_pedestrian.getPosition() );

        // add car to the agent's list
        if ( l_ped.m_start_cycle == 0 )
        {
            m_environment.initialset(l_pedestrian);
            // add pedestrian to the pedestrian's list
            m_environment.initialPedestrian(l_pedestrian);
        }
        else
        {
            m_environment.addPedtoInitializeLater().computeIfAbsent( l_ped.m_start_cycle, k -> new ArrayList<>()).add(l_pedestrian);
        }
        l_pedestrian.setTrue( l_ped.m_start_cycle );
        //System.out.println( l_pedestrian.getname()+"id"+l_pedestrian.getPosition() +"ped_start_end"+ l_pedestrian.getGoalposition());

        return l_pedestrian;

    }
}
