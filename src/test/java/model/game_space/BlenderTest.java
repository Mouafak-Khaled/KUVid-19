package model.game_space;

import model.game_running.ProjectileContainer;
import org.junit.jupiter.api.Test;
import services.exceptions.ContainerNotInitializedException;

import static org.junit.jupiter.api.Assertions.*;

class BlenderTest {

    @Test
    void blend() {

        ProjectileContainer container = new ProjectileContainer(null, 5, 5, 5, 5);
        Blender blender = new Blender(container);

        try {
        //Black Box tests.
        assertArrayEquals(container.getAtomMap(), new int[]{5, 5, 5, 5});

        blender.blendAtoms(0, 1, 1); // Blending 2 ALPHA into 1 BETA
        assertArrayEquals(container.getAtomMap(), new int[]{3, 6, 5, 5});

        blender.blendAtoms(0, 2, 1); // Blending 3 ALPHA into 1 GAMMA
        assertArrayEquals(container.getAtomMap(), new int[]{0, 6, 6, 5});

        blender.blendAtoms(0, 1, 1);  // Blending 3 ALPHA into 1 GAMMA;
                                                                                //  Not enough atoms to blend.
        assertArrayEquals(container.getAtomMap(), new int[]{0, 6, 6, 5});


        blender.blendAtoms(1, 2, 3);  // Blending 2 BETA into 1 GAMMA three

        assertArrayEquals(container.getAtomMap(), new int[]{0, 0, 9, 5});       // times.

        blender.blendAtoms(1, 2, 1);  // Blending 2 BETA into 1 GAMMA
        assertArrayEquals(container.getAtomMap(), new int[]{0, 0, 9, 5});       // Not enough atoms to blend.

        blender.blendAtoms(2, 3, 5);  // Blending 2 GAMMA into 1 SIGMA five
        assertArrayEquals(container.getAtomMap(), new int[]{0, 0, 1, 9});       // times. Could only blend 4 times.
                                                                                // Not enough atoms to blend.

        container = new ProjectileContainer(null,0,0,0,0);
        assertArrayEquals(container.getAtomMap(), new int[]{0, 0, 0, 0});

        blender.blendAtoms(1, 3, 1);  // Blending 4 ALPHA into 1 SIGMA
        assertArrayEquals(container.getAtomMap(), new int[]{0, 0, 0, 0});       //  Empty container.

        blender.blendAtoms(2, 3, 1);  // Blending 3 BETA into 1 SIGMA
        assertArrayEquals(container.getAtomMap(), new int[]{0, 0, 0, 0});       //  Empty container.

        blender.blendAtoms(3, 3, 1);  // Blending 2 GAMMA into 1 SIGMA
        assertArrayEquals(container.getAtomMap(), new int[]{0, 0, 0, 0});       //  Empty container.

        //Glass Box tests.
        assertThrows(ContainerNotInitializedException.class,
                () -> new Blender(null).blendAtoms(1,2,1));

        assertThrows(ContainerNotInitializedException.class,
                () -> new Blender(null).blendAtoms(4,1,4));

        } catch (ContainerNotInitializedException e) {
            e.printStackTrace();
        }
    }
}