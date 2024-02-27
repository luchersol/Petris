import AppNavbar from "./AppNavbar";
import { render, screen } from "./test-utils";

describe('AppNavbar', () => {

    test('renders public links correctly', () => {
        render(<AppNavbar />);
        const linkDocsElement = screen.getByRole('link', { name: 'Documentación' });
        expect(linkDocsElement).toBeInTheDocument();

        const linkPlansElement = screen.getByRole('link', { name: 'Qué es Petris' });
        expect(linkPlansElement).toBeInTheDocument();

        const linkHomeElement = screen.getByRole('link', { name: 'logo' });
        expect(linkHomeElement).toBeInTheDocument();
    });

    test('renders not user links correctly', () => {
        render(<AppNavbar />);
        const linkDocsElement = screen.getByRole('link', { name: 'Crear cuenta' });
        expect(linkDocsElement).toBeInTheDocument();

        const linkPlansElement = screen.getByRole('link', { name: 'Iniciar sesión' });
        expect(linkPlansElement).toBeInTheDocument();
    });

});
