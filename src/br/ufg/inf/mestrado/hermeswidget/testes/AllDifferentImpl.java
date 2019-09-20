package br.ufg.inf.mestrado.hermeswidget.testes;

import java.util.Iterator;

import com.hp.hpl.jena.enhanced.EnhGraph;
import com.hp.hpl.jena.enhanced.EnhNode;
import com.hp.hpl.jena.enhanced.Implementation;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.AllDifferent;
import com.hp.hpl.jena.ontology.ConversionException;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Profile;
import com.hp.hpl.jena.ontology.impl.OntResourceImpl;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Map1;

/**
* <p>
* Implementation of the abstraction of axioms that denote the single name assumption.
* </p>
*/
public class AllDifferentImpl extends OntResourceImpl implements AllDifferent
{
	
	/**
     * A factory for generating AllDifferent facets from nodes in enhanced graphs.
     * Note: should not be invoked directly by user code: use
     * {@link com.hp.hpl.jena.rdf.model.RDFNode#as as()} instead.
     */
    public static Implementation factory = new Implementation() {
        @Override
        public EnhNode wrap( Node n, EnhGraph eg ) {
            if (canWrap( n, eg )) {
                return new AllDifferentImpl( n, eg );
            }
            else {
                throw new ConversionException( "Cannot convert node " + n + " to AllDifferent");
            }
        }

        @Override
        public boolean canWrap( Node node, EnhGraph eg ) {
            // node will support being an AllDifferent facet if it has rdf:type owl:AllDifferent or equivalent
            Profile profile;
			if (eg instanceof OntModel)
				profile = ((OntModel) eg).getProfile();
			else
				profile = null;
            return (profile != null)  &&  profile.isSupported( node, eg, AllDifferent.class );
        }
    };


	public AllDifferentImpl(Node n, EnhGraph g) {
		super(n, g);
		
	}

	// External signature methods
    //////////////////////////////////

    /**
     * <p>Assert that the list of distinct individuals in this AllDifferent declaration
     * is the given list. Any existing
     * statements for <code>distinctMembers</code> will be removed.</p>
     * @param members A list of the members that are declared to be distinct.
     * @exception ProfileException If the {@link Profile#DISTINCT_MEMBERS()} property is not supported in the current language profile.
     */
    @Override
    public void setDistinctMembers( RDFList members ) {
        setPropertyValue( getProfile().DISTINCT_MEMBERS(), "DISTINCT_MEMBERS", members );
    }

    /**
     * <p>Add the given individual to the list of distinct members of this AllDifferent declaration.</p>
     * @param res A resource that will be added to the list of all different members.
     * @exception ProfileException If the {@link Profile#DISTINCT_MEMBERS()} property is not supported in the current language profile.
     */
    @Override
    public void addDistinctMember( Resource res ) {
        addListPropertyValue( getProfile().DISTINCT_MEMBERS(), "DISTINCT_MEMBERS", res );
    }

    /**
     * <p>Add the given individuals to the list of distinct members of this AllDifferent declaration.</p>
     * @param individuals An iterator over the distinct invididuals that will be added
     * @exception ProfileException If the {@link Profile#DISTINCT_MEMBERS()} property is not supported in the current language profile.
     */
    @Override
    public void addDistinctMembers( Iterator<? extends Resource> individuals ) {
        while (individuals.hasNext()) {
            addDistinctMember( individuals.next() );
        }
    }

    /**
     * <p>Answer the list of distinct members for this AllDifferent declaration.</p>
     * @return The list of individuals declared distinct by this AllDifferent declaration.
     * @exception ProfileException If the {@link Profile#DISTINCT_MEMBERS()} property is not supported in the current language profile.
     */
    @Override
    public RDFList getDistinctMembers() {
        return objectAs( getProfile().DISTINCT_MEMBERS(), "DISTINCT_MEMBERS", RDFList.class );
    }

    /**
     * <p>Answer an iterator over all of the individuals that are declared to be distinct by
     * this AllDifferent declaration. Each element of the iterator will be an {@link OntResource}.</p>
     * @return An iterator over distinct individuals.
     * @exception ProfileException If the {@link Profile#DISTINCT_MEMBERS()} property is not supported in the current language profile.
     */
    @Override
    public ExtendedIterator<? extends OntResource> listDistinctMembers() {
        return getDistinctMembers().mapWith( new Map1<RDFNode,OntResource>() {
            @Override
            public OntResource map1( RDFNode o ) {
                return ((Resource) o).as( OntResource.class );
            }} );
    }

    /**
     * <p>Answer true if this AllDifferent declaration includes <code>res</code> as one of the distinct individuals.</p>
     * @param res A resource to test against
     * @return True if <code>res</code> is declared to be distinct from the other individuals in this declation.
     * @exception ProfileException If the {@link Profile#DISTINCT_MEMBERS()} property is not supported in the current language profile.
     */
    @Override
    public boolean hasDistinctMember( Resource res ) {
        return getDistinctMembers().contains( res );
    }

    /**
     * <p>Remove the given resource from the list of distinct individuals.  If this statement
     * is not true of the current model, nothing happens.</p>
     * @param res A resource that is no longer distinct from the other listed individuals
     */
    @Override
    public void removeDistinctMember( Resource res ) {
        setDistinctMembers( getDistinctMembers().remove( res ) );
    }
}